/* ==========================================
 * Laverca Project
 * https://sourceforge.net/projects/laverca/
 * ==========================================
 * Copyright 2015 Laverca Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fi.laverca.util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Transform java strings so that they only contain
 * characters included in the GSM03.38 charset.
 * See FiCom 2.0, appendix D.
 */ 
public class FiComCharsetLimiter {

    private static HashMap<Character,String> okChars;
    
    static {
        okChars = createGsm0338Charset();
    }

    private static void register(final Map<Character,String> charset, final char latin, final String gsmda) {
        charset.put(Character.valueOf(latin), gsmda);
    }
    private static HashMap<Character,String> createGsm0338Charset() {
        HashMap<Character,String> charset = new HashMap<>();

        register(charset,'\u0040',"\u0000"); // 0x00 0 COMMERCIAL AT @ 0x40
        register(charset,'\u00A3',"\u0001"); // 0x01 1 POUND SIGN Â£ 0xC2A3
        register(charset,'\u0024',"\u0002"); // 0x02 2 DOLLAR SIGN $ 0x24
        register(charset,'\u00A5',"\u0003"); // 0x03 3 YEN SIGN Â¥ 0xC2A5
        register(charset,'\u00E8',"\u0004"); // 0x04 4 LATIN SMALL LETTER E WITH GRAVE Ã¨ 0xC3A8
        register(charset,'\u00E9',"\u0005"); // 0x05 5 LATIN SMALL LETTER E WITH ACUTE Ã© 0xC3A9
        register(charset,'\u00F9',"\u0006"); // 0x06 6 LATIN SMALL LETTER U WITH GRAVE Ã¹ 0xC3B9
        register(charset,'\u00EC',"\u0007"); // 0x07 7 LATIN SMALL LETTER I WITH GRAVE Ã¬ 0xC3AC
        register(charset,'\u00F2',"\u0008"); // 0x08 8 LATIN SMALL LETTER O WITH GRAVE Ã² 0xC3B2
        register(charset,'\u00C7',"\u0009"); // 0x09 9 LATIN CAPITAL LETTER C WITH CEDILLA Ã‡ 0xC387
        register(charset,'\n',"\n");         // 0x0A 10 LINE FEED 0x0A //note Java limitation; the UTF-16 code is \u000A
        register(charset,'\u00D8',"\u000B"); // 0x0B 11 LATIN CAPITAL LETTER O WITH STROKE Ã˜ 0xC398
        register(charset,'\u00F8',"\u000C"); // 0x0C 12 LATIN SMALL LETTER O WITH STROKE Ã¸ 0xC3B8
        register(charset,'\r',"\r");         // 0x0D 13 CARRIAGE RETURN 0x0D //note Java limitation; the UTF-16 code is \u000D
        register(charset,'\u00C5',"\u000E"); // 0x0E 14 LATIN CAPITAL LETTER A WITH RING ABOVE Ã… 0xC385
        register(charset,'\u00E5',"\u000F"); // 0x0F 15 LATIN SMALL LETTER A WITH RING ABOVE Ã¥ 0xC3A5
        register(charset,'\u0394',"\u0010"); // 0x10 16 GREEK CAPITAL LETTER DELTA ô€‡» 0xCE94
        register(charset,'\u005F',"\u0011"); // 0x11 17 LOW LINE _ 0x5F
        register(charset,'\u03A6',"\u0012"); // 0x12 18 GREEK CAPITAL LETTER PHI ô€„­ 0xCEA6
        register(charset,'\u0393',"\u0013"); // 0x13 19 GREEK CAPITAL LETTER GAMMA ô€„« 0xCE93
        register(charset,'\u039B',"\u0014"); // 0x14 20 GREEK CAPITAL LETTER LAMBDA ô€ˆ� 0xCE9B
        register(charset,'\u03A9',"\u0015"); // 0x15 21 GREEK CAPITAL LETTER OMEGA ô€ˆ� 0xCEA9
        register(charset,'\u03A0',"\u0016"); // 0x16 22 GREEK CAPITAL LETTER PI ô€ˆ† 0xCEA0
        register(charset,'\u03A8',"\u0017"); // 0x17 23 GREEK CAPITAL LETTER PSI ô€ˆŒ 0xCEA8
        register(charset,'\u03A3',"\u0018"); // 0x18 24 GREEK CAPITAL LETTER SIGMA ô€ˆˆ 0xCEA3
        register(charset,'\u0398',"\u0019"); // 0x19 25 GREEK CAPITAL LETTER THETA ô€„¬ 0xCE98
        register(charset,'\u039E',"\u001A"); // 0x1A 26 GREEK CAPITAL LETTER XI ô€ˆ„ 0xCE9E
        //register(charset,'\u0000'); // 0x1B 27 ESCAPE TO EXTENSION TABLE -novalue-
        register(charset,'\u000C',"\u001B\n"); // 0x1B0A 27 10 FORM FEED 0x0C
        register(charset,'\u005E',"\u001B\u0014"); // 0x1B14 27 20 CIRCUMFLEX ACCENT ^ 0x5E
        register(charset,'\u007B',"\u001B\u0028"); // 0x1B28 27 40 LEFT CURLY BRACKET { 0x7B
        register(charset,'\u007D',"\u001B\u0029"); // 0x1B29 27 41 RIGHT CURLY BRACKET } 0x7D
        register(charset,'\\',    "\u001B\u002F"); // 0x1B2F 27 47 REVERSE SOLIDUS (BACKSLASH) \ 0x5C // the UTF-16 code is \u005C
        register(charset,'\u005B',"\u001B\u003C"); // 0x1B3C 27 60 LEFT SQUARE BRACKET [ 0x5B
        register(charset,'\u007E',"\u001B\u003D"); // 0x1B3D 27 61 TILDE ~ 0x7E
        register(charset,'\u005D',"\u001B\u003E"); // 0x1B3E 27 62 RIGHT SQUARE BRACKET ] 0x5D
        register(charset,'\u007C',"\u001B\u0040"); // 0x1B40 27 64 VERTICAL BAR | 0x7C
        register(charset,'\u20AC',"\u001B\u0065"); // 0x1B65 27 101 EURO SIGN â‚¬ 0xE282AC
        register(charset,'\u00C6',"\u001C"); // 0x1C 28 LATIN CAPITAL LETTER AE Ã† 0xC386
        register(charset,'\u00E6',"\u001D"); // 0x1D 29 LATIN SMALL LETTER AE Ã¦ 0xC3A6
        register(charset,'\u00DF',"\u001E"); // 0x1E 30 LATIN SMALL LETTER SHARP S (German) ÃŸ 0xC39F
        register(charset,'\u00C9',"\u001F"); // 0x1F 31 LATIN CAPITAL LETTER E WITH ACUTE Ã‰ 0xC389
        register(charset,'\u0020',"\u0020"); // 0x20 32 SPACE 0x20
        register(charset,'\u0021',"\u0021"); // 0x21 33 EXCLAMATION MARK ! 0x21
        register(charset,'\u0022',"\"");     // 0x22 34 QUOTATION MARK " 0x22
        register(charset,'\u0023',"\u0023"); // 0x23 35 NUMBER SIGN # 0x23
        register(charset,'\u00A4',"\u0024"); // 0x24 36 CURRENCY SIGN Â¤ 0xC2A4
        register(charset,'\u0025',"\u0025"); // 0x25 37 PERCENT SIGN % 0x25
        register(charset,'\u0026',"\u0026"); // 0x26 38 AMPERSAND & 0x26
        register(charset,'\'',"'");     // 0x27 39 APOSTROPHE ' 0x27 // the UTF-16 code is \u0027
        register(charset,'\u0028',"\u0028"); // 0x28 40 LEFT PARENTHESIS ( 0x28
        register(charset,'\u0029',"\u0029"); // 0x29 41 RIGHT PARENTHESIS ) 0x29
        register(charset,'\u002A',"\u002A"); // 0x2A 42 ASTERISK * 0x2A
        register(charset,'\u002B',"\u002B"); // 0x2B 43 PLUS SIGN + 0x2B
        register(charset,'\u002C',"\u002C"); // 0x2C 44 COMMA , 0x2C
        register(charset,'\u002D',"\u002D"); // 0x2D 45 HYPHENMINUS 0x2D
        register(charset,'\u002E',"\u002E"); // 0x2E 46 FULL STOP . 0x2E
        register(charset,'\u002F',"\u002F"); // 0x2F 47 SOLIDUS (SLASH) / 0x2F
        register(charset,'\u0030',"\u0030"); // 0x30 48 DIGIT ZERO 0 0x30
        register(charset,'\u0031',"\u0031"); // 0x31 49 DIGIT ONE 1 0x31
        register(charset,'\u0032',"\u0032"); // 0x32 50 DIGIT TWO 2 0x32
        register(charset,'\u0033',"\u0033"); // 0x33 51 DIGIT THREE 3 0x33
        register(charset,'\u0034',"\u0034"); // 0x34 52 DIGIT FOUR 4 0x34
        register(charset,'\u0035',"\u0035"); // 0x35 53 DIGIT FIVE 5 0x35
        register(charset,'\u0036',"\u0036"); // 0x36 54 DIGIT SIX 6 0x36
        register(charset,'\u0037',"\u0037"); // 0x37 55 DIGIT SEVEN 7 0x37
        register(charset,'\u0038',"\u0038"); // 0x38 56 DIGIT EIGHT 8 0x38
        register(charset,'\u0039',"\u0039"); // 0x39 57 DIGIT NINE 9 0x39
        register(charset,'\u003A',"\u003A"); // 0x3A 58 COLON : 0x3A
        register(charset,'\u003B',"\u003B"); // 0x3B 59 SEMICOLON ; 0x3B
        register(charset,'\u003C',"\u003C"); // 0x3C 60 LESSTHANSIGN < 0x3C
        register(charset,'\u003D',"\u003D"); // 0x3D 61 EQUALS SIGN = 0x3D
        register(charset,'\u003E',"\u003E"); // 0x3E 62 GREATERTHANSIGN > 0x3E
        register(charset,'\u003F',"\u003F"); // 0x3F 63 QUESTION MARK ? 0x3F
        register(charset,'\u00A1',"\u0040"); // 0x40 64 INVERTED EXCLAMATION MARK Â¡ 0xC2A1
        register(charset,'\u0041',"\u0041"); // 0x41 65 LATIN CAPITAL LETTER A A 0x41
        register(charset,'\u0042',"\u0042"); // 0x42 66 LATIN CAPITAL LETTER B B 0x42
        register(charset,'\u0043',"\u0043"); // 0x43 67 LATIN CAPITAL LETTER C C 0x43
        register(charset,'\u0044',"\u0044"); // 0x44 68 LATIN CAPITAL LETTER D D 0x44
        register(charset,'\u0045',"\u0045"); // 0x45 69 LATIN CAPITAL LETTER E E 0x45
        register(charset,'\u0046',"\u0046"); // 0x46 70 LATIN CAPITAL LETTER F F 0x46
        register(charset,'\u0047',"\u0047"); // 0x47 71 LATIN CAPITAL LETTER G G 0x47
        register(charset,'\u0048',"\u0048"); // 0x48 72 LATIN CAPITAL LETTER H H 0x48
        register(charset,'\u0049',"\u0049"); // 0x49 73 LATIN CAPITAL LETTER I I 0x49
        register(charset,'\u004A',"\u004A"); // 0x4A 74 LATIN CAPITAL LETTER J J 0x4A
        register(charset,'\u004B',"\u004B"); // 0x4B 75 LATIN CAPITAL LETTER K K 0x4B
        register(charset,'\u004C',"\u004C"); // 0x4C 76 LATIN CAPITAL LETTER L L 0x4C
        register(charset,'\u004D',"\u004D"); // 0x4D 77 LATIN CAPITAL LETTER M M 0x4D
        register(charset,'\u004E',"\u004E"); // 0x4E 78 LATIN CAPITAL LETTER N N 0x4E
        register(charset,'\u004F',"\u004F"); // 0x4F 79 LATIN CAPITAL LETTER O O 0x4F
        register(charset,'\u0050',"\u0050"); // 0x50 80 LATIN CAPITAL LETTER P P 0x50
        register(charset,'\u0051',"\u0051"); // 0x51 81 LATIN CAPITAL LETTER Q Q 0x51
        register(charset,'\u0052',"\u0052"); // 0x52 82 LATIN CAPITAL LETTER R R 0x52
        register(charset,'\u0053',"\u0053"); // 0x53 83 LATIN CAPITAL LETTER S S 0x53
        register(charset,'\u0054',"\u0054"); // 0x54 84 LATIN CAPITAL LETTER T T 0x54
        register(charset,'\u0055',"\u0055"); // 0x55 85 LATIN CAPITAL LETTER U U 0x55
        register(charset,'\u0056',"\u0056"); // 0x56 86 LATIN CAPITAL LETTER V V 0x56
        register(charset,'\u0057',"\u0057"); // 0x57 87 LATIN CAPITAL LETTER W W 0x57
        register(charset,'\u0058',"\u0058"); // 0x58 88 LATIN CAPITAL LETTER X X 0x58
        register(charset,'\u0059',"\u0059"); // 0x59 89 LATIN CAPITAL LETTER Y Y 0x59
        register(charset,'\u005A',"\u005A"); // 0x5A 90 LATIN CAPITAL LETTER Z Z 0x5A
        register(charset,'\u00C4',"\u005B"); // 0x5B 91 LATIN CAPITAL LETTER A WITH DIAERESIS Ã„ 0xC384
        register(charset,'\u00D6',"\\");     // 0x5C 92 LATIN CAPITAL LETTER O WITH DIAERESIS Ã– 0xC396
        register(charset,'\u00D1',"\u005D"); // 0x5D 93 LATIN CAPITAL LETTER N WITH TILDE Ã‘ 0xC391
        register(charset,'\u00DC',"\u005E"); // 0x5E 94 LATIN CAPITAL LETTER U WITH DIAERESIS Ãœ 0xC39C // suspicious
        register(charset,'\u00A7',"\u005F"); // 0x5F 95 SECTION SIGN Â§ 0xC2A7
        register(charset,'\u00BF',"\u0060"); // 0x60 96 INVERTED QUESTION MARK Â¿ 0xC2BF
        register(charset,'\u0061',"\u0061"); // 0x61 97 LATIN SMALL LETTER A a 0x61
        register(charset,'\u0062',"\u0062"); // 0x62 98 LATIN SMALL LETTER B b 0x62
        register(charset,'\u0063',"\u0063"); // 0x63 99 LATIN SMALL LETTER C c 0x63
        register(charset,'\u0064',"\u0064"); // 0x64 100 LATIN SMALL LETTER D d 0x64
        register(charset,'\u0065',"\u0065"); // 0x65 101 LATIN SMALL LETTER E e 0x65
        register(charset,'\u0066',"\u0066"); // 0x66 102 LATIN SMALL LETTER F f 0x66
        register(charset,'\u0067',"\u0067"); // 0x67 103 LATIN SMALL LETTER G g 0x67
        register(charset,'\u0068',"\u0068"); // 0x68 104 LATIN SMALL LETTER H h 0x68
        register(charset,'\u0069',"\u0069"); // 0x69 105 LATIN SMALL LETTER I i 0x69
        register(charset,'\u006A',"\u006A"); // 0x6A 106 LATIN SMALL LETTER J j 0x6A
        register(charset,'\u006B',"\u006B"); // 0x6B 107 LATIN SMALL LETTER K k 0x6B
        register(charset,'\u006C',"\u006C"); // 0x6C 108 LATIN SMALL LETTER L l 0x6C
        register(charset,'\u006D',"\u006D"); // 0x6D 109 LATIN SMALL LETTER M m 0x6D
        register(charset,'\u006E',"\u006E"); // 0x6E 110 LATIN SMALL LETTER N n 0x6E
        register(charset,'\u006F',"\u006F"); // 0x6F 111 LATIN SMALL LETTER O o 0x6F
        register(charset,'\u0070',"\u0070"); // 0x70 112 LATIN SMALL LETTER P p 0x70
        register(charset,'\u0071',"\u0071"); // 0x71 113 LATIN SMALL LETTER Q q 0x71
        register(charset,'\u0072',"\u0072"); // 0x72 114 LATIN SMALL LETTER R r 0x72
        register(charset,'\u0073',"\u0073"); // 0x73 115 LATIN SMALL LETTER S s 0x73
        register(charset,'\u0074',"\u0074"); // 0x74 116 LATIN SMALL LETTER T t 0x74
        register(charset,'\u0075',"\u0075"); // 0x75 117 LATIN SMALL LETTER U u 0x75
        register(charset,'\u0076',"\u0076"); // 0x76 118 LATIN SMALL LETTER V v 0x76
        register(charset,'\u0077',"\u0077"); // 0x77 119 LATIN SMALL LETTER W w 0x77
        register(charset,'\u0078',"\u0078"); // 0x78 120 LATIN SMALL LETTER X x 0x78
        register(charset,'\u0079',"\u0079"); // 0x79 121 LATIN SMALL LETTER Y y 0x79
        register(charset,'\u007A',"\u007A"); // 0x7A 122 LATIN SMALL LETTER Z z 0x7A
        register(charset,'\u00E4',"\u007B"); // 0x7B 123 LATIN SMALL LETTER A WITH DIAERESIS Ã¤ 0xC3A4
        register(charset,'\u00F6',"\u007C"); // 0x7C 124 LATIN SMALL LETTER O WITH DIAERESIS Ã¶ 0xC3B6
        register(charset,'\u00F1',"\u007D"); // 0x7D 125 LATIN SMALL LETTER N WITH TILDE Ã± 0xC3B1
        register(charset,'\u00FC',"\u007E"); // 0x7E 126 LATIN SMALL LETTER U WITH DIAERESIS Ã¼ 0xC3BC
        register(charset,'\u00E0',"\u007F"); // 0x7F 127 LATIN SMALL LETTER A WITH GRAVE Ã  0xC3A0
        return charset;
    }
    
    public static boolean areGSMcharactersOnly(String inputString) {
        if (inputString == null) {
            return true;
        }
        final char[] inputChars  = inputString.toCharArray();

        for (int i = 0; i < inputChars.length; i++) {
            final Character input = Character.valueOf(inputChars[i]);
            if (!okChars.containsKey(input)) {
                return false;
            }
        }
        return true;
    }

    public static String limit(String inputString) {

        if (inputString == null) {
            return null;
        }
        
        char[] inputChars  = inputString.toCharArray();
        char[] outputChars = new char[inputChars.length];

        for (int i = 0; i < inputChars.length; i++) {
            Character input = Character.valueOf(inputChars[i]);
            char output = '?';
            if (okChars.containsKey(input)) {
                output = input.charValue();
            }
            outputChars[i] = output;
        }

        return new String(outputChars);
    }


    public static byte[] toGSM0338(String inputString) {

        if (inputString == null) {
            return null;
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream(2+inputString.length());
        baos.write(0x04);

        char[] inputChars  = inputString.toCharArray();
        for (char input : inputChars) {
            String s = okChars.get(Character.valueOf(input));
            for (char c : s.toCharArray()) {
                baos.write(c);
            }
        }
        return baos.toByteArray();
    }

    /**
     * Convert the input java String UCS2 byte array per
     * ETSI TS 123 038 + ETSI TS 102223 "Text string" format.
     * @param inputString String to convert
     * @return UCS2 byte[]
     */
    public static byte[] toUCS2(String inputString) {

        if (inputString == null) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream(2+2*inputString.length());
        baos.write(0x08);

        char[] inputChars  = inputString.toCharArray();
        for (char c : inputChars) {
            baos.write((c >>> 8) & 0xFF); // note big-endian byte order!
            baos.write(c & 0xFF);
        }
        return baos.toByteArray();
    }
}


