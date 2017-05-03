package test.cs;

import org.w3c.dom.Document;

import com.cs.base.xml.XMLManager;
import com.cs.ee.core.cache.EECacheManager;

/**
 * @author King
 *
 */
public class HSBCCSTestConst {
	
	public static Document trxDom = null;
	/**
	 * 
	 */
	public static void init() throws Exception {
		
		if(trxDom == null){
			trxDom = XMLManager.xmlFileToDom("./test/EETrxDom.xml");
		}
		EECacheManager.init("./test/EE_Cache_Config.xml");
	}	

}
