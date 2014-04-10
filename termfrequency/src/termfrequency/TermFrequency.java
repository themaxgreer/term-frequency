package termfrequency;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class TermFrequency {
	
	private static HashMap<String,Integer> themap;
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0){
			System.out.println("Missing argument # of strings to print exiting.");
			System.exit(1);
		}
		themap = new HashMap<>();
		ValueComparator thecomp = new ValueComparator(themap);
		TreeMap<String, Integer> sortedMap = new TreeMap<>(thecomp);
		int count = Integer.parseInt(args[0]);
		String ROOT = "HW3-Corpus-OANC/";
		FileVisitor<Path> fileProcessor = new ProcessFile();
		Files.walkFileTree(Paths.get(ROOT), fileProcessor);
		sortedMap.putAll(themap);
		Iterator entries = sortedMap.entrySet().iterator();
		for(int i = 0; i < count; i++){
			if(!entries.hasNext()){
				break;
			}
			Entry theEntry = (Entry)entries.next();
			Object key = theEntry.getKey();
			Object value = theEntry.getValue();
			System.out.println(key + " " + value);
		}
	}
	
	private static class ValueComparator implements Comparator<String>{		
		Map<String, Integer> base;
		
		public ValueComparator(Map<String, Integer> base){
			this.base = base;
		}
		
		@Override
		public int compare(String o1, String o2) {
			if(base.get(o1) >= base.get(o2)){
				return -1;
			}else{
				return 1;
			}
		}
	}
	
	private static final class ProcessFile extends SimpleFileVisitor<Path>{
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			String path = file.toString();
			Scanner thescanner = new Scanner(new File(path)).useDelimiter("\\Z");
			//if statement because corpus was given to me by a mac
			if(!path.contains(".DS_Store")){
				if(thescanner.hasNext()){
					String output = thescanner.next();
					//AnyCharNotIn[WordCharacter WhiteSpaceCharacter]
					output = output.replaceAll("[^\\w\\s]", "").toLowerCase();
					StringTokenizer st = new StringTokenizer(output);
					while(st.hasMoreTokens()){
						String token = st.nextToken();
						if(themap.containsKey(token)){
							themap.put(token, (Integer)themap.get(token) + 1);
						}else{
							themap.put(token, 1);
						}
					}
				}
			}
			return FileVisitResult.CONTINUE;
		}
	}
}

