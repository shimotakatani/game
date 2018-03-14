package engine.objects.units;

import java.util.Comparator;

/**
 * create time 09.03.2018
 *
 * @author nponosov
 */
public class RabbitComporator implements Comparator<Rabbit> {
    @Override
    public int compare(Rabbit o1, Rabbit o2) {
        return o2.eatedGrass - o1.eatedGrass;
    }
}
