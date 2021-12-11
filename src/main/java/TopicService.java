import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String valueText = null;
        String text = "";
        String status = "204";
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<String> queue;
        if ("POST".equals(req.getHttpRequestType())) {
            map = topic.get(req.getSourceName());
            for (ConcurrentLinkedQueue<String> q : map.values()) {
                q.add(req.getParam());
            }
            status = "200";
        } else if ("GET".equals(req.getHttpRequestType())) {
            topic.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            if (topic.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>()) == null) {
                status = "200";
            }
            valueText = topic.get(req.getSourceName()).get(req.getParam()).poll();
        }

        if (valueText != null) {
            text = valueText;
            status = "200";
        }
        return new  Resp(text, status);
    }
}

