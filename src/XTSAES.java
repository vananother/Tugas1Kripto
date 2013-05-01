import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Anang Ferdi Kusuma (1006665952)
 * @author Arief Yudha Satria (1006665984)
 * @author Evan Ariansyah H (1006671381)
 */
public class XTSAES {

    AES aes;
    File i;
    File k;
    File o;
    private static byte[] tweak = Util.hex2byte("0123456789abcdef0123456789abcdef");
    byte[] key1;
    byte[] key2;
    byte[][] alphaTable;
    byte[] plain;

    public XTSAES(File input, File key, File output, int mode) {
        aes = new AES();
        i = input;
        k = key;
        o = output;
    }

    public void start() {
        try {
            System.out.println(0x100);
            FileReader fr = new FileReader(k);
            BufferedReader buff = new BufferedReader(fr);
            String temp = buff.readLine();
            key1 = temp.substring(0, 16).getBytes();
            key2 = temp.substring(16, 32).getBytes();
            aes.setKey(key2);
            String input = "";
            fr = new FileReader(i);
            buff = new BufferedReader(fr);
            temp = buff.readLine();
            if (temp != null) {
                input = temp;
            }
            while ((temp = buff.readLine()) != null) {
                input = input + "\r" + temp;
            }
            System.out.println(input);
            plain = input.getBytes();
            alphaTable = new byte[plain.length][16];
            fillAlphaTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] xtsBlockEncrypt(byte[] plain, int j) {
        byte[] t = mul(aes.encrypt(tweak), alphaTable[j]);
        byte[] pp = xor(plain, t);
        aes.setKey(key1);
        byte[] cc = aes.encrypt(pp);
        byte[] cj = xor(cc, t);
        return cj;
    }

    public byte[] xtsBlockDecrypt(byte[] cipher, int j) {
        byte[] t = mul(aes.encrypt(tweak), alphaTable[j]);
        byte[] cc = xor(cipher, t);
        aes.setKey(key1);
        byte[] pp = aes.decrypt(cc);
        byte[] pj = xor(pp, t);
        return pj;
    }

    private void fillAlphaTable() {
        alphaTable[0] = new byte[16];
        alphaTable[0][0] = 1;
        alphaTable[1] = new byte[16];
        alphaTable[1][0] = 2;
        for (int i = 2; i < plain.length; i++) {
            byte[] temp = new byte[16];
            temp[0] = (byte) ((alphaTable[i - 1][0] << 1) % 128
                    ^ 135 * (alphaTable[i - 1][15] >>> 7));
            for (int j = 1; j < 16; j++) {
                temp[j] = (byte) ((alphaTable[i - 1][j] << 1) % 128
                        ^ (alphaTable[i - 1][j - 1] >>> 7));
            }
            alphaTable[i] = temp;
        }
    }

    private byte[] mul(byte[] a, byte[] b) {
        byte[] res = new byte[a.length];
        return res;
    }

    private byte[] xor(byte[] a, byte[] b) {
        byte[] res = new byte[a.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) (a[i] ^ b[i]);
        }
        return res;
    }
}
