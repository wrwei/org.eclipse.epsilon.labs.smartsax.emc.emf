package org.eclipse.epsilon.labs.smartsax.emc.emf.indexed;

import java.util.HashMap;

public class TestHashMap {

	public static void main(String[] args) {
		
		
		TestHashMap t1 = new TestHashMap();
		TestHashMap t2 = new TestHashMap();
		TestHashMap t3 = new TestHashMap();
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		
		map.put(1.1, t1);
		map.put(2.2, t2);
		map.put(3.3, t3);
		map.put(1.1, null);
		
		System.out.println(map.keySet());
		System.out.println(map.values());
		
		for(Object f: map.keySet())
		{
			if ((Double)f >= 2.2) {
				System.out.println(map.get(f));
			}
		}
	}
}
