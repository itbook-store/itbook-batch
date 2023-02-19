package shop.itbook.itbookbatch.membership.listener;

import java.util.List;
import javax.batch.api.chunk.listener.ItemWriteListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 노수연
 * @since 1.0
 */
@Slf4j
public class CustomMembershipWriterListener implements ItemWriteListener {

    @Override
    public void beforeWrite(List<Object> list) {
        log.debug("membership item write start!");
    }

    @Override
    public void afterWrite(List<Object> list) {

        log.debug("membership item write end!");
    }

    @Override
    public void onWriteError(List<Object> list, Exception e) {
        log.error("membership item write error! error : " + e.getMessage());
        e.printStackTrace();
    }
}
