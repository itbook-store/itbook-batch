package shop.itbook.itbookbatch.membership.listener;

import javax.batch.api.chunk.listener.ItemReadListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 노수연
 * @since 1.0
 */
@Slf4j
public class CustomMembershipReadListener implements ItemReadListener {

    @Override
    public void beforeRead() {
        log.debug("membership item read start!");
    }

    @Override
    public void afterRead(Object o) {
        log.debug("membership item read end!");

    }

    @Override
    public void onReadError(Exception e){
        log.error("membership item read error! error : " + e.getMessage());
        e.printStackTrace();
    }
}
