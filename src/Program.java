import java.util.*;

public class Program {
	private Scanner s;
	private int[][] tree = new int[10000][2];
	private String[] node = new String[10000];

	public Program(){
		s = new Scanner(System.in);
		this.launch();
	}

	private void launch(){
		cin("Welcome\nThis program translates valid mathematical expressions\ninto expressions in Reverse Polish Notation.\nFor its correct operation you must make sure that you write valid expressions.\nJust use the parentheses as a grouping sign.\n\nPress enter to continue . . .");
		char alive = '@';
		do{
			String exp = cin("Write a valid expression:\n==> ");

			exp = this.noSpace(exp);

			exp = this.clearParentesis(exp);

			boolean flag = this.validate(exp);

			if (!flag)continue;
			
			this.createTree(exp, -1, 0);

			String sol = this.dfs(0);

			tree = new int[10000][2];
			node = new String[10000];
			
			String t = cin("The Reverse Polish Notation of the expression is\n\n==>   " + sol + "\n\n\nPress 'q' to exit or ENTER to continue. . .\n\n\n");
			if (t.length() != 0) alive = t.charAt(0);
		}while(alive != 'Q' && alive != 'q');

		cls();
		cout("Thank you for using our program.\n\n\n");
	}

	private String noSpace(String exp){
		String temp = "";
		for (int i = 0; i < exp.length(); i++){
			if (exp.charAt(i) == ' ')continue;
			temp += exp.charAt(i);
		}
		return temp;
	}
	
	private int searchPair(String s, int pos){
		Stack<Integer> stack = new Stack<>();
		stack.push(pos);
		for (int i = pos+1; i < s.length(); i++){
			if (s.charAt(i) == '(') stack.push(i);
			else if (s.charAt(i) == ')'){
				stack.pop();
				if (stack.isEmpty()) return i;
			}
		}
		return -1;
	}

	private boolean validate(String exp){
		boolean flag = false;

		char t;
		
		for (int i = 0; i < exp.length(); i++){
			t = exp.charAt(i);
			if (t == '('){
				if (flag)return false;
				int pos = searchPair(exp, i);
				if (pos == -1) return false;
				if (!validate(exp.substring(i+1, pos))) return false;
				i = pos;
				flag = true;
			}
			else if (t == ')')return false;
			else if (t == '^' || t == '*' || t == '/' || t == '+' || t == '-'){
				if (!flag)return false;
				flag = false;
			}
			else if (('a' <= t && t <= 'z') || ('A' <= t && t <= 'Z')){
				if (flag)return false;
				flag = true;
			}
			else if ('0' <= t && t <= '9'){
				if (flag)return false;
				flag = true;
				for (;i+1 < exp.length() && '0' <= exp.charAt(i+1) && exp.charAt(i+1) <= '9'; i++);
			}
			else{
				return false;
			}

		}
		return flag;
	}

	private int searchBridge(String exp){
		int c = 0;
		for (int i = 0; i < exp.length(); i++){
			if (c == 0 && (exp.charAt(i) == '+' || exp.charAt(i) == '-'))return i;
			else if (exp.charAt(i) == '(')c++;
			else if (exp.charAt(i) == ')')c--;
		}

		c = 0;
		for (int i = 0; i < exp.length(); i++){
			if (c == 0 && (exp.charAt(i) == '*' || exp.charAt(i) == '/'))return i;
			else if (exp.charAt(i) == '(')c++;
			else if (exp.charAt(i) == ')')c--;
		}

		c = 0;
		for (int i = 0; i < exp.length(); i++){
			if (c == 0 && exp.charAt(i) == '^')return i;
			else if (exp.charAt(i) == '(')c++;
			else if (exp.charAt(i) == ')')c--;
		}

		return 0;
	}

	private int createTree(String exp, int father, int counter){
		exp = this.clearParentesis(exp);

		if (father != -1){
			if (tree[father][0] == 0)tree[father][0] = counter;
			else tree[father][1] = counter;
		}
		
		int bridge = searchBridge(exp);
		if (bridge == 0){
			node[counter] = exp;
			return counter;
		}
		else node[counter] = exp.substring(bridge, bridge+1);

		return createTree(exp.substring(bridge+1, exp.length()), counter, createTree(exp.substring(0, bridge), counter, counter+1)+1);
	}

	private String clearParentesis(String exp){
		while (exp.length() != 0 && exp.charAt(0) == '(' && this.searchPair(exp, 0) == exp.length()-1)
			exp = exp.substring(1, exp.length()-1);
		return exp;
	}
	
	private String dfs(int index){
		if (tree[index][0] == 0)return node[index];

		String one = dfs(tree[index][0]);

		String two = dfs(tree[index][1]);

		return one + " " + two + " " + node[index];
	}

	private void cls(){
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	private void cout(String msg){
		System.out.print(msg);
	}

	private String cin(String msg){
		if (!msg.equals("-1")){
			cls();
			cout(msg);
		}
		return s.nextLine();
	}
}
