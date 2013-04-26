
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Anang Ferdi Kusuma, Arief Yudha Satria, Evan Ariansyah H
 */
public class XTSAES {

    AES aes;
    File i;
    File k;
    File o;
    private static byte[] tweak = Util.hex2byte("0123456789abcdef0123456789abcdef");
    byte[] key1;
    byte[] key2;

    public XTSAES(File input, File key, File output, int mode) {
        aes = new AES();
        i = input;
        k = key;
        o = output;
    }

    public void start() {
        try {
            FileReader fr = new FileReader(k);
            BufferedReader buff = new BufferedReader(fr);
            String temp = buff.readLine();
            key1 = temp.substring(0, 16).getBytes();
            key2 = temp.substring(16, 32).getBytes();
            System.out.println(key1.length);
            System.out.println(key2.length);
            aes.setKey(key2);

            String input= "";
            fr = new FileReader(i);
            buff = new BufferedReader(fr);
            while ((temp = buff.readLine()) != null) {
                System.out.println(temp);
                input = input + temp;
            }
            System.out.println(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] xtsBlockEncrypt(byte[] plain) {
        byte[] temp = aes.encrypt(tweak);
        //temp = AES.mul(a, b);
        return temp;
    }

    public byte[] xtsBlockDecrypt(byte[] cipher) {
        byte[] temp = aes.encrypt(tweak);
        //temp = AES.mul(a, b);
        return temp;
    }
}
