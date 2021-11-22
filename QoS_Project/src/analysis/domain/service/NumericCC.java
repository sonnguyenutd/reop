package analysis.domain.service;

import java.util.ArrayList;
import java.util.List;

import analysis.domain.service.obj.Obj;

public class NumericCC {
	public String content;
	public List<Function> participants;

	public NumericCC(String content, List<Obj> paras) {
		this.content = content;
		this.participants = new ArrayList<Function>();
		extractPaticipants(paras);
	}

	/*
	 * (op funct1 funct2/value)
	 */
	private void extractPaticipants(List<Obj> paras) {
		content = content.trim();
		String temp = content.substring(1, content.length() - 1);
		int i = 0, j = i;
		while (true) {
			i = temp.indexOf("(", j);
			if (i == -1)
				break;
			j = temp.indexOf(")", i + 1);
			if (j == -1 || j <= i)
				break;
			
			String f = temp.substring(i, j + 1);
			Function funct = new Function(f, paras);
			participants.add(funct);
//			System.out.println("--->" + funct);
			i = j + 1;
		}
	}
	
	@Override
	public String toString() {
		return content;
	}
}
