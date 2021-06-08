package com.praxis.developer.livestreams;

import java.util.stream.LongStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
public class LivestreamsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivestreamsApplication.class, args);
	}

}

@Component
@RequiredArgsConstructor
class Producer {

	private final KafkaTemplate<Long, String> kafkaTemplate;

	public void produce() {
		LongStream.range(0, 10).forEach(i -> {
			kafkaTemplate.send("livestreams", i, "livestreams" + i).addCallback(result -> {
				if (result != null) {
					final long offset = result.getRecordMetadata().offset();
					final long partiton = result.getRecordMetadata().partition();
					System.out.println("Offset " + offset + "Partition " + partiton);
				}
			}, ex -> System.err.println("not today"));
		});

	}
}
