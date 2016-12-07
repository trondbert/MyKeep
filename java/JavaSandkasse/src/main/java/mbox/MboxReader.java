package mbox;

import org.apache.james.mime4j.dom.Header;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.mboxiterator.CharBufferWrapper;
import org.apache.james.mime4j.mboxiterator.MboxIterator;
import org.apache.james.mime4j.message.DefaultMessageBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MboxReader {

    Charset charset = StandardCharsets.ISO_8859_1;

    DefaultMessageBuilder defaultMessageBuilder = new DefaultMessageBuilder();

    public static void main(String[] args) {

        try {
            FileWriter outFile = new FileWriter("./out.txt");
            new MboxReader().read(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read(FileWriter outFile) {
        String downloads = "/Users/trond/Downloads/";
        String file = downloads + "Takeout/Google Mail/All e-post inkludert søppelpost og e-poster i papi.mbox";
        try {
            MboxIterator builder = MboxIterator.fromFile(file).charset(charset).build();
            int count = 0;
            for (CharBufferWrapper charBufferWrapper : builder) {
                InputStream inputStream = charBufferWrapper.asInputStream(charset);

                char read = (char) inputStream.read();
                while (read != (char)-1) {
                    outFile.write(read);
                    read = (char) inputStream.read();
                }
                //Header message = defaultMessageBuilder.parseHeader(inputStream);
                /*System.out.println(message.getFilename());
                System.out.println(message.getSubject());
                System.out.println(message.getSender());
                System.out.println(message.getBody());*/
                //count++;
            }
            outFile.close();
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
