package shop.itbook.itbookbatch.grade.membershippoint.listener;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

/**
 * @author : 최겸준
 * @since 1.0
 */
@Slf4j
public class CustomPointWriteListener implements ItemWriteListener {

    @Override
    public void beforeWrite(List list) {
        log.debug("point item write start!");
    }

    @Override
    public void afterWrite(List list) {
        log.debug("point item write end!");
    }

    @Override
    public void onWriteError(Exception e, List list) {
        log.error("point item write error! error : " + e.getMessage());
    }
}
