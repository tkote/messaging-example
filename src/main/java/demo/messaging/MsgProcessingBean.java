package demo.messaging;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import io.helidon.common.configurable.ThreadPoolSupplier;

@ApplicationScoped
public class MsgProcessingBean {
    private final static Logger logger = Logger.getLogger(MsgProcessingBean.class.getName());

    // usging ForkJoinPool.commonPool() implicitly
    // private final SubmissionPublisher<Message> publisher = new SubmissionPublisher<>();

    // using helidon's thread pool
    private final SubmissionPublisher<Message> publisher = new SubmissionPublisher<>(
        ThreadPoolSupplier.builder().threadNamePrefix("messaging-").build().get(),
        Flow.defaultBufferSize()
    );

    @Outgoing("test-channel")
    public Publisher<Message> preparePublisher() {
        return ReactiveStreams
                .fromPublisher(FlowAdapters.toPublisher(publisher))
                .buildRs();
    }

    @Incoming("test-channel")
    public void consume(Message message) {
        /* DO ACTUAL JOB HERE!! */
        logger.info(String.format("Consuming Message: key=%s, value=%s", message.getKey(), message.getValue()));
    }

    public int submit(Message message){
        return publisher.submit(message);
    }

}
