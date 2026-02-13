package sdm.freedom.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class AgentFactory {

    private static final Map<String, Supplier<AbstractAgent>> AGENT_REGISTRY = new HashMap<>();

    static {
        AGENT_REGISTRY.put("Player", HumanAgent::new);
        AGENT_REGISTRY.put("Random", RandomAgent::new);
        //further agent concepts:
        //AGENT_REGISTRY.put("AI", AIAgent::new);
    }

    public static AbstractAgent create(String type){
        Supplier<AbstractAgent> supplier = AGENT_REGISTRY.get(type);
        if (supplier == null) throw new IllegalArgumentException("Unknown agent type: " + type);
        return supplier.get();
    }

    public static Set<String> availableAgents() {
        return AGENT_REGISTRY.keySet();
    }
}

