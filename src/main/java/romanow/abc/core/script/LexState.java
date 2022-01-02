package romanow.abc.core.script;

import romanow.abc.core.utils.Pair;

public abstract class LexState {
    public abstract LexState onEvent(char sym);
}
