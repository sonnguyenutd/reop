import subprocess
import threading
import os,signal
import psutil
import random

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




benchmark = "."


pddlFiles = []
# r=root, d=directories, f = files
for r, d, f in os.walk(benchmark):
    for file in f:
        if '.pddl' in file and 'reduced' not in file:
            pddlFiles.append(os.path.join(r, file))

mode = "em-fdr-ts"
fd = "../../cpddl/bin/pddl-fdr --"+mode+" -o"

random.shuffle(pddlFiles)

def findProbs(containingDir,pddlFiles):
    probFiles = []
    for other in pddlFiles:
        head, tail = os.path.split(other)
        probName = tail.replace(".pddl","")

        outfile = containingDir+"/"+probName+"_"+mode+".out"
        if other.startswith(containingDir) and not os.path.isfile(outfile) and not other.endswith("domain.pddl"):
            probFiles.append(other)
    return probFiles

for f in pddlFiles:
    if f.endswith("domain.pddl"):
        domain = f
        containingDir = domain.replace("domain.pddl","")
        probFiles = findProbs(containingDir,pddlFiles)
        probFiles.sort()
        for prob in probFiles:
            print(prob)
            date_time = Command('date').run(capture=True)
            print (date_time)
           
            head, tail = os.path.split(prob)
            probName = tail.replace(".pddl","")
            outfile = containingDir+""+probName+"_"+mode+".out"
            print (fd+" "+outfile+" "+domain+ " "+prob)
            Command(fd+" "+outfile+" "+domain+ " "+prob).run(timeout=900)
            if os.path.isfile(outfile):
                break

        
