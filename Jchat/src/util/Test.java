package util;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SubstanceFieldOfWheatLookAndFeel;
     
public class Test {

        /**
         * @param args
         * @throws Exception 
         */
        public static void main(String[] args) throws Exception {
        	//命令字+用户名+':'+主机名+':'+消息正文   01DELL:NeverMore:I am on
        	SubstanceFieldOfWheatLookAndFeel lf = new SubstanceFieldOfWheatLookAndFeel();
        	System.out.println(lf instanceof SubstanceLookAndFeel);
        }

     
    }