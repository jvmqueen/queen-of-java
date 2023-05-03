package org.queenlang.helloworld;

import com.example.other.Test;
import com.other.*;
import com.example.other.Other;
import com.example.other.more.*;

@Marker
@com.test.Marker2
@com.jjj.Simple("value")
@com.asdasd.Normal(a = "1", b = 2, c = 3l)
@com.asdasd.other.Normal(a = "1", b = 2, c = 3l, arr = { 1, 2, 3 })
@Simple("value")
@SimpleArray({ 1, 2, 3 })
@Simple2(2 + 3)
@Normal(a = "1", b = 2, c = 3l, d = 4 + 5)
public final class EmptyAnnotatedClass implements SomeInterface {
}
