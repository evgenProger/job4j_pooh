import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
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
        }
        if ("GET".equals(req.getHttpRequestType())) {
            if (!topic.containsKey(req.getSourceName())) {
                queue = new ConcurrentLinkedQueue<>();
                map.putIfAbsent(req.getParam(), queue);
                topic.put(req.getSourceName(), map);

            } else {
                if (!topic.get(req.getSourceName()).containsKey(req.getParam())) {
                    queue = new ConcurrentLinkedQueue<>();
                    map = topic.get(req.getSourceName());
                    map.put(req.getParam(), queue);
                    status = "200";
                }
            }
            String valueText = topic.get(req.getSourceName()).get(req.getParam()).poll();
            if (valueText != null) {
                text = valueText;
                status = "200";
            }
        }
        return new Resp(text, status);
    }
}
