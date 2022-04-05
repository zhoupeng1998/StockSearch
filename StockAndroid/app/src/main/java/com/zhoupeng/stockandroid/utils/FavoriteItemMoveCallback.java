package com.zhoupeng.stockandroid.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.zhoupeng.stockandroid.R;

public class FavoriteItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract adapter;
    private Context context;

    private Paint clearPaint;
    private ColorDrawable removeBackground;
    private int removeBackgroundColor;
    private Drawable removeDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;

    public FavoriteItemMoveCallback(Context context, ItemTouchHelperContract adapter) {
        this.context = context;
        this.adapter = adapter;

        this.removeBackground = new ColorDrawable();
        this.removeBackgroundColor = Color.parseColor("#b80f0a");
        this.clearPaint = new Paint();
        this.clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.removeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_delete_outline_24);
        this.intrinsicHeight = removeDrawable.getIntrinsicHeight();
        this.intrinsicWidth = removeDrawable.getIntrinsicWidth();
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        adapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        adapter.onItemRemove(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();
        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        removeBackground.setColor(removeBackgroundColor);
        removeBackground.setBounds(itemView.getRight() + (int) dX,
                itemView.getTop(), itemView.getRight(), itemView.getBottom());
        removeBackground.draw(c);

        int removeIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int removeIconMargin = (itemHeight - intrinsicHeight) / 2;
        int removeIconLeft = itemView.getRight() - removeIconMargin - intrinsicWidth;
        int removeIconRight = itemView.getRight() - removeIconMargin;
        int removeIconBottom = removeIconTop + intrinsicHeight;

        removeDrawable.setBounds(removeIconLeft, removeIconTop, removeIconRight, removeIconBottom);
        removeDrawable.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, clearPaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}
