package shop.itbook.itbookbatch.grade.membershippoint.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

/**
 * @author : 최겸준
 * @since 1.0
 */
@Slf4j
public class CustomPointReadListener implements ItemReadListener {
    
    @Override
    public void beforeRead() {
        log.debug("point item read start!");
    }

    @Override
    public void afterRead(Object o) {
        log.debug("point item read end!");
    }

    @Override
    public void onReadError(Exception e) {
        log.error("point item read error! error : " + e.getMessage());
    }
}
