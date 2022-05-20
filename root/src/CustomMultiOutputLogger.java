import java.io.IOException;
import java.io.OutputStream;

public class CustomMultiOutputLogger extends OutputStream {

    OutputStream[] outputs;

    public CustomMultiOutputLogger(OutputStream... streams) {
        this.outputs = streams;
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream out : outputs) {
            out.write(b);
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        for (OutputStream out : outputs) {
            out.write(b);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        for (OutputStream out : outputs) {
            out.write(b, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        for (OutputStream out : outputs) {
            out.flush();
        }
    }

    @Override
    public void close() throws IOException {
        for (OutputStream out : outputs) {
            out.close();
        }
    }
}
