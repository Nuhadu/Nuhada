package data;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class AffinitySelector implements SortedMap<Integer, String>{
	private HashMap<Integer, String> map;
	
	public AffinitySelector(){
		map = new HashMap<Integer, String>();
	}
	
	public AffinitySelector(Integer[] affs, String[] strs){
		this();
		assert(affs.length == strs.length);
		for(int i = 0; i < affs.length; i++)
			map.put(affs[i], strs[i]);
	}
	
	public AffinitySelector(String[] str){
		this();
		int pas = 100/str.length;
		int i = pas;
		for(String s : str){
			map.put(i, s);
			i+=pas;
		}
	}
	
	
	public String getFromAffinity(int affinity){
		String str = null;		
		str = get(headMap(affinity).lastKey());
		if(str == null)
			str = get(firstKey());
		return str;
	}
	
	//OOVERRIDE METHODS	
	@Override
	public void clear() {
		map.clear();
		
	}

	@Override
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return map.containsValue(arg0);
	}

	@Override
	public String get(Object arg0) {
		return map.get(arg0);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public String put(Integer key, String value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends String> m) {
		map.putAll(m);
	}

	@Override
	public String remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Comparator<? super Integer> comparator() {
		Comparator<Integer> comp = new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {					
				if(o1 < o2) return -1;
				else if( o1 == o2 ) return 0;
				else return 1;
			}
		};
		return comp;
	}

	@Override
	public Set<java.util.Map.Entry<Integer, String>> entrySet() {			
		return map.entrySet();
	}

	@Override
	public Integer firstKey() {
		Integer firstKey = Affinity.MAX_AFFINITY;
		for(Integer key : keySet())
			if( comparator().compare(firstKey, key) > 0)
				firstKey = key;
		return firstKey;
	}

	@Override
	public SortedMap<Integer, String> headMap(Integer toKey) {
		AffinitySelector headMap = new AffinitySelector();
		for(Integer key : keySet() ){
			if( comparator().compare(key, toKey) < 0)
				headMap.put(key, get(key));
		}
		return headMap;
	}

	@Override
	public Set<Integer> keySet() {			
		return map.keySet();
	}

	@Override
	public Integer lastKey() {
		Integer lastKey = Affinity.MIN_AFFINITY;
		for(Integer key : map.keySet())
			if( comparator().compare(lastKey, key) < 0)
				lastKey = key;
		
		return lastKey;
	}

	@Override
	public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
		AffinitySelector subMap = new AffinitySelector();
		for(Integer key : keySet() ){
			if( comparator().compare(key, fromKey) >= 0 && comparator().compare(key, fromKey) < 0 ){
				subMap.put(key, get(key));
				continue;
			}
			if(comparator().compare(key, fromKey) <= 0 && comparator().compare(key, fromKey) > 0)
				subMap.put(key, get(key));
		}
		return subMap;
	}

	@Override
	public SortedMap<Integer, String> tailMap(Integer fromKey) {
		AffinitySelector tailMap = new AffinitySelector();
		for(Integer key : keySet() ){
			if( comparator().compare(key, fromKey) >= 0)
				tailMap.put(key, get(key));
		}
		return tailMap;
	}

	@Override
	public Collection<String> values() {			
		return map.values();
	}
}
