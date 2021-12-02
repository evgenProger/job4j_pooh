import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic
            = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp = null;
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<String> queue;
        if ("POST".equals(req.getHttpRequestType())) {
           map = topic.get(req.getSourceName());
           for (ConcurrentLinkedQueue<String> q: map.values()) {
                 q.add(req.getParam());
           }
        }
        if ("GET".equals(req.getHttpRequestType())) {
            if (topic.size() == 0) {
                queue = new ConcurrentLinkedQueue<>();
                map.putIfAbsent(req.getParam(), queue);
                topic.putIfAbsent(req.getSourceName(), map);
            } else {
                String status = null;

                if (!topic.get(req.getSourceName()).containsKey(req.getParam())) {
                    queue = new ConcurrentLinkedQueue<>();
                    map = topic.get(req.getSourceName());
                    map.put(req.getParam(), queue);
                }
                String text = topic.get(req.getSourceName()).get(req.getParam()).poll();
                if (text == null) {
                    text = "";
                    status = "404";
                } else {
                    status = "200";
                }
                resp = new Resp(text, status);
            }
        }
        return resp;
    }
}
