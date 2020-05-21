package myflink;

import myflink.deserializationSchema.UserSchema;
import myflink.model.DeptTotalSalary;
import myflink.model.User;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.util.Collector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FlinkKafkaConsumer {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
    // only required for Kafka 0.8
        properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "test");

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());


        DataStreamSource<User> stream = env
                .addSource(new FlinkKafkaConsumer09<>("user",new UserSchema(), properties));

        SingleOutputStreamOperator<DeptTotalSalary> dataStream = stream.map(new parseRow())
                .keyBy("dept")
                .countWindowAll(5)
                .sum("salary");

       /* WindowedStream<DeptTotalSalary, Tuple, GlobalWindow> windowedStream =
                stream.map(new parseRow())
                .keyBy("staticKey")
                .countWindow(5);

        DataStream<Map<String,Long>> outDataStream = windowedStream.apply(new collectDeptSal());*/
        dataStream.print();

        env.execute("kafta");
    }


    public static class parseRow implements MapFunction<User,  DeptTotalSalary > {
        @Override
        public DeptTotalSalary map(User value) throws Exception {
            return new DeptTotalSalary(value.getDept(),value.getSalary());
        }
    }


    public static class collectDeptSal implements WindowFunction<DeptTotalSalary, Map<String,Long>, Tuple, GlobalWindow>{
        @Override
        public void apply(Tuple tuple, GlobalWindow window, Iterable<DeptTotalSalary> input,
                          Collector<Map<String, Long>> out) throws Exception {

            Map<String,Long> output = new HashMap<>();
            input.forEach((user -> {
                if(output.containsKey(user.getDept())){
                    output.put(user.getDept(),output.get(user.getDept())+user.getSalary());
                }else {
                    output.put(user.getDept(),output.get(user.getSalary()));
                }
            }));
            out.collect(output);
        }

    }


}
