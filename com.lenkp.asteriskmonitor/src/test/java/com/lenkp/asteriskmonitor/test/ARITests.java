package com.lenkp.asteriskmonitor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.ARI.ClassFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.ActionAsterisk;
import ch.loway.oss.ari4java.generated.ActionBridges;
import ch.loway.oss.ari4java.generated.ari_1_9_0.actions.ActionAsterisk_impl_ari_1_9_0;
import ch.loway.oss.ari4java.generated.ari_1_9_0.actions.ActionBridges_impl_ari_1_9_0;
import ch.loway.oss.ari4java.tools.ARIException;

/**
 * Generate random UID
 * 
 * @author mbahhalim
 *
 */
public class ARITests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * An example abstract to concrete builder.
	 */
	public static class SampleClassFactory implements ClassFactory {
	
		@SuppressWarnings("rawtypes")
		@Override
		public Class getImplementationFor(Class interfaceClass) {
		    
			if ( interfaceClass.equals(ActionBridges.class) ) {
				return ActionBridges_impl_ari_1_9_0.class;
			}
			else {
				return null;
			}
		}
		
	}

	@Test
	public void testImplementationFactory() {
		ClassFactory factory = new SampleClassFactory();
		    
		assertEquals("ActionBridges", ActionBridges_impl_ari_1_9_0.class, factory.getImplementationFor(ActionBridges.class));
		assertEquals("Not found", null, factory.getImplementationFor(String.class));
	}
    
	@Test
	public void testBuildAction() throws ARIException {
		ARI ari = new ARI();
		ari.setVersion(AriVersion.ARI_1_9_0);
		
		ActionAsterisk asterisk = ari.asterisk();
		assertTrue( "Correct type", asterisk instanceof ActionAsterisk_impl_ari_1_9_0 );    
	}
    
	@Test
	public void testCreateUid() throws ARIException {
		String v = ARI.getUID();
		
		System.out.println("UID: "+v);
		assertTrue( "UID created", v.length() > 0);
	}

}
