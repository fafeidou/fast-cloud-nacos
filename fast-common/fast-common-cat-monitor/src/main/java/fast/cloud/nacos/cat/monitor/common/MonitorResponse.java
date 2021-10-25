package fast.cloud.nacos.cat.monitor.common;


import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MonitorResponse extends HttpServletResponseWrapper {
    private int status;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private HttpServletResponse response;

    public MonitorResponse(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    int getCurrentStatus() {
        return this.status;
    }

    public byte[] getBody() {
        return this.byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStreamWrapper(this.byteArrayOutputStream, this.response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(this.byteArrayOutputStream, this.response.getCharacterEncoding()));
    }

    @Override
    public void reset() {
        super.reset();
        this.status = 0;
    }

    @Override
    public void sendError(int error) throws IOException {
        super.sendError(error);
        this.status = error;
    }

    @Override
    public void sendError(int error, String message) throws IOException {
        super.sendError(error, message);
        this.status = error;
    }

    @Override
    public void setStatus(int status) {
        super.setStatus(status);
        this.status = status;
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {
        private ByteArrayOutputStream outputStream;
        private HttpServletResponse response;

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener listener) {
        }

        @Override
        public void write(int b) throws IOException {
            this.outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            if (!this.response.isCommitted()) {
                byte[] body = this.outputStream.toByteArray();
                ServletOutputStream outputStream = this.response.getOutputStream();
                outputStream.write(body);
                outputStream.flush();
            }

        }

        public ByteArrayOutputStream getOutputStream() {
            return this.outputStream;
        }

        public HttpServletResponse getResponse() {
            return this.response;
        }

        public void setOutputStream(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public void setResponse(HttpServletResponse response) {
            this.response = response;
        }

        @Override
        public String toString() {
            return "MonitorResponse.ServletOutputStreamWrapper(outputStream=" + this.getOutputStream() + ", response=" + this.getResponse() + ")";
        }

        public ServletOutputStreamWrapper(ByteArrayOutputStream outputStream, HttpServletResponse response) {
            this.outputStream = outputStream;
            this.response = response;
        }
    }
}