package org.queenlang.helloworld;

import com.example.other.Test;
import com.other.*;
import com.example.other.Other;
import com.example.other.more.*;

@Marker
@Simple("value")
@Simple2(2 + 3)
@Normal(a = "1", b = 2, c = 3l, d = 4 + 5)
public interface EmptyAnnotatedInterface extends OtherInterface {
}
