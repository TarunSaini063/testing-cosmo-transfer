package sample;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

final public class FileReceiver {

    private final int port;
    private final FileWriter fileWriter;
    private final long size;

    public FileReceiver(final int port, final FileWriter fileWriter, final long size) {
        this.port = port;
        this.fileWriter = fileWriter;
        this.size = size;
    }

    void receive() throws IOException {
        SocketChannel channel = null;

        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            init(serverSocketChannel);
            channel = serverSocketChannel.accept();
            doTransfer(channel);
        } finally {
            if (!Objects.isNull(channel)) {
                channel.close();
            }

            this.fileWriter.close();
        }
    }

    private void doTransfer(final SocketChannel channel) throws IOException {
        assert !Objects.isNull(channel);

        this.fileWriter.transfer(channel, this.size);
    }

    private void init(final ServerSocketChannel serverSocketChannel) throws IOException {
        assert !Objects.isNull(serverSocketChannel);

        serverSocketChannel.bind(new InetSocketAddress(this.port));
    }
}
