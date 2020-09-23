package cobble.core.test.combine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CombineBean {

    private int index;

    private double scoreSum;

    /**
     * 组合中各个元素的候选素材Index
     */
    private int[] candidateIndexArr;


}
