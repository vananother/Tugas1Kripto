
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
 * @author van
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
            key1 = Util.hex2byte(temp.substring(0, 8));
            key2 = Util.hex2byte(temp.substring(8, 16));
            aes.setKey(key2);

            fr = new FileReader(i);
            buff = new BufferedReader(fr);
            while ((temp = buff.readLine()) != null) {
            }
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
