package ovenues.com.ovenue.cart.CartSummaryServicesStickyList;

import android.content.Context;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;

public final class TopSnappedStickyLayoutManagerSummaryServiceList extends StickyLayoutManager {

    public TopSnappedStickyLayoutManagerSummaryServiceList(Context context, StickyHeaderHandler headerHandler) {
        super(context, headerHandler);
    }

    @Override public void scrollToPosition(int position) {
        super.scrollToPositionWithOffset(position, 0);
    }
}
