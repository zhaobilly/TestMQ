package test.cs;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.cs.core.utility.CompressUtil;

public class TestAA {
	public static void main(String[] args) {
		try {
			String strvalue = FileUtils.readFileToString(new File("c://1.txt"), "utf-8");

			strvalue = CompressUtil.decompressString2String(strvalue);
			System.out.println(strvalue);
			System.out.println(strvalue.length());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

