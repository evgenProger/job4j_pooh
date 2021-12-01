import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        ConcurrentLinkedQueue<String> concurrentQueue = new ConcurrentLinkedQueue<>();

        if ("POST".equals(req.getHttpRequestType())) {
            concurrentQueue.add(req.getSourceName());


        }
        return null;
    }
}
