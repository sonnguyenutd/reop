import subprocess
import threading
import os,signal
import random
import psutil

""" Run system commands with timeout
"""
def kill(proc_pid):
    process = psutil.Process(proc_pid)
    for proc in process.children(recursive=True):
        proc.kill()
    process.kill()

class Command(object):
    def __init__(self, cmd):
        self.cmd = cmd
        self.process = None
        self.out = None

    def run_command(self, capture = False):
        if not capture:
            self.process = subprocess.Popen(self.cmd, shell=True)
            self.process.communicate()
            return
        # capturing the outputs of shell commands
        self.process = subprocess.Popen(self.cmd, stdout=subprocess.PIPE, shell=True)
        out,err = self.process.communicate()
        if len(out) > 0:
            self.out = out.splitlines()
        else:
            self.out = None

    # set default timeout to 2 minutes
    def run(self, capture = False, timeout = 120):
        thread = threading.Thread(target=self.run_command, args=(capture,))
        thread.start()
        thread.join(timeout)
        if thread.is_alive():
            print ("Command timeout, kill it: " + self.cmd)
            # self.process.kill()
            kill(self.process.pid)
            thread.join()
        return self.out

'''basic test cases'''

# # run shell command without capture
# Command('pwd').run()
# # capture the output of a command
# date_time = Command('date').run(capture=True)
# print (date_time)

# Command('echo "sleep 10 seconds"; sleep 10; echo "done"').run(timeout=2)

import os

def findProbs(containingDir,pddlFiles):
    probFiles = []
    for other in pddlFiles:
        if other.startswith(containingDir) and not os.path.isfile(other.replace(".pddl", "_ops.txt")) and not other.endswith("domain.pddl"):
            probFiles.append(other)
    return probFiles


benchmark = "."


pddlFiles = []
# r=root, d=directories, f = files
for r, d, f in os.walk(benchmark):
    for file in f:
        if '.pddl' in file:
            pddlFiles.append(os.path.join(r, file))
fd = "/Users/sonnguyen/Downloads/downward-main/fast-downward.py --alias lama-first"

random.shuffle(pddlFiles)
for f in pddlFiles:
    if f.endswith("domain.pddl"):
        domain = f
        containingDir = domain.replace("domain.pddl","")
        probFiles = findProbs(containingDir,pddlFiles)
        probFiles.sort()
        for prob in probFiles:
            print(prob)
            Command(fd+" "+domain+" "+prob).run(timeout=500)
        