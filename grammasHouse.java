import acm.program.*;
import java.util.*;

public class grammasHouse extends ConsoleProgram{

	public void run(){
		while(true){
			getSentence();
			tokenizer = new StringTokenizer(sentence, DELIMETERS, true);
			translateSentence();
			println(translatedSentence);
			println();
		}
	}
	
	private void getSentence(){
		sentence = readLine("Enter a sentence to translate: ");
		 
		
	}
	
	private void translateSentence(){
		translatedSentence = "";
		while (tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			if (isWord(token)){
				token = translateWord(token);
			}
			translatedSentence += token;
		}
			
	}
	
	private boolean isWord(String str){
		for (int i = 0; i < str.length(); i++){
			char c = str.charAt(i);
			if (!Character.isLetter(c)) return false;
		}
		return true;
	}
	
	private String translateWord(String str){
		int firstVowel = firstVowel(str);
		if (firstVowel == -1) return str;
		else if (firstVowel == 0) return str + "way";
		else{
			String head = str.substring(0,firstVowel(str));
			String tail = str.substring(firstVowel(str), str.length());
			str = tail + head + "ay";
		}
		return str;
	}
	
	
	private int firstVowel(String str){
		for(int i = 0; i<str.length(); i++){
			char c = str.charAt(i);
			switch (Character.toLowerCase(c)){
				case ('a'): case ('e'): case ('i'): case ('o'): case ('u'): 
					return i;
			}
		}
		return -1;
	}
	
	StringTokenizer tokenizer;
	private String sentence, translatedSentence;
	private static final String DELIMETERS = " `~!@#$%^&*()_-+={[}]|\\:;\"'<>,.?/";
}
