package app.spaulding.kafka.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

import static app.spaulding.kafka.streams.utils.Streamutils.*;

/**
 * Kakfa Streams ML Keras app.
 *
 * @author joshua.spaulding
 *
 */
public class StreamsStarterApp {

    public static void main(String[] args) throws Exception {

        // load the model
        String simpleMlp = new ClassPathResource("model/dga.h5").getFile().getPath();
        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMlp);

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kstream-keras");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> kStream = builder.stream("streams-input");

        kStream.foreach(((key, value) -> {
            if(value != null && !value.equals("")) {
                // Preprocess domain name
                INDArray features = process(value);
                // Make prediction
                double prediction = model.output(features).getDouble(0);
                System.out.println(prediction);
                System.out.println(value);
            }
        }));

        kStream.to("streams-output");

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.cleanUp(); // only do this in dev - not in prod
        streams.start();

        // print the topology
        System.out.println(streams.localThreadsMetadata().toString());

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

    private static INDArray process(String domain) {

        int maxLen = 63;
        double threshold = 0.5;
        List classes = Arrays.asList("legit", "dga");
        List headers = Arrays.asList("input","preprocessed","probability","threshold","class");

        /* Process domain
            1. extract tld
            2. convert char to int
            3. pad int[] to common length
        */
        domain = tldextract(domain);
        float[] domainArray = preprocess(domain);
        domainArray = pad(domainArray, maxLen);

        return Nd4j.create(domainArray);
    }

}
