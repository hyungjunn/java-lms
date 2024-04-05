package nextstep.courses.domain;

import nextstep.courses.domain.image.Image;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.courses.domain.image.Type.GIF;

public class ImageTest {

    @DisplayName("이미지 크기가 1MB를 초과하면 예외를 반환한다")
    @Test
    void capacityException() {
        Assertions.assertThatThrownBy(() -> new Image(0, GIF, 300))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지크기는 1MB를 초과할 수 없다");
    }

    @DisplayName("이미지 width가 300픽셀미만이면 예외를 반환한다")
    @Test
    void widthException() {
        Assertions.assertThatThrownBy(() -> new Image(5, GIF, 200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미지 width는 300픽셀이상이어야 한다");
    }


}
