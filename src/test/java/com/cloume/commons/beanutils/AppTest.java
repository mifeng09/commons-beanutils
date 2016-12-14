package com.cloume.commons.beanutils;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	class InnerClass {
		String someValue;
	}
	
	class ObjectToUpdate {
		int intValue;
		long longValue;
		String stringValue;
		Object[] arrayValue;
		InnerClass classValue;
	}
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testUpdater()
    {
    	InnerClass iclass = new InnerClass();
    	Map<String, Object> from = new HashMap<String, Object>();
    	from.put("intValue", 100);
    	from.put("longValue", 200L);
    	from.put("stringValue", "this is string");
    	from.put("classValue", iclass);
    	
    	ObjectToUpdate updated = Updater.wrap(new ObjectToUpdate()).update(from);
    	assertNull(updated.arrayValue);
    	assertEquals(100, updated.intValue);
    	assertEquals(200L, updated.longValue);
    	assertEquals("this is string", updated.stringValue);
    	assertEquals(iclass, updated.classValue);
    }
    
    public void testUpdaterWithConverter()
    {
        assertTrue( true );
    }
}
