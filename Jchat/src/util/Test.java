package util;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SubstanceFieldOfWheatLookAndFeel;
     
public class Test {

        /**
         * @param args
         * @throws Exception 
         */
        public static void main(String[] args) throws Exception {
        	//������+�û���+':'+������+':'+��Ϣ����   01DELL:NeverMore:I am on
        	SubstanceFieldOfWheatLookAndFeel lf = new SubstanceFieldOfWheatLookAndFeel();
        	System.out.println(lf instanceof SubstanceLookAndFeel);
        }

     
    }