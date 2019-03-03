package app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class Application {

    @Value("${local.server.port}")

    public static void main(String[] args) throws UnknownHostException {
        String ip = InetAddress.getLocalHost().getHostAddress();
        System.out.println(ip);
        SpringApplication.run(Application.class, args);
    }
}
