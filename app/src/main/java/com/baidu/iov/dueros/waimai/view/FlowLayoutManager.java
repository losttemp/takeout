package com.baidu.iov.dueros.waimai.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutManager extends RecyclerView.LayoutManager {

	private static final String TAG = FlowLayoutManager.class.getSimpleName();

	private final FlowLayoutManager self = this;

	protected int width, height;
	private int left, top, right;

	private int usedMaxWidth;
	private int verticalScrollOffset = 0;

	private int totalHeight = 0;
	private Row row = new Row();
	private List<Row> lineRows = new ArrayList<>();

	private SparseArray<Rect> allItemFrames = new SparseArray<>();

	public int getTotalHeight() {
		return totalHeight;
	}

	public FlowLayoutManager() {
		setAutoMeasureEnabled(true);
	}

	public class Item {
		int useHeight;
		View view;

		void setRect(Rect rect) {
			this.rect = rect;
		}

		Rect rect;

		Item(int useHeight, View view, Rect rect) {
			this.useHeight = useHeight;
			this.view = view;
			this.rect = rect;
		}
	}

	public class Row {
		void setCuTop(float cuTop) {
			this.cuTop = cuTop;
		}

		public void setMaxHeight(float maxHeight) {
			this.maxHeight = maxHeight;
		}

		float cuTop;
		float maxHeight;
		List<Item> views = new ArrayList<>();

		void addViews(Item view) {
			views.add(view);
		}
	}

	@Override
	public RecyclerView.LayoutParams generateDefaultLayoutParams() {
		return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
		totalHeight = 0;
		int cuLineTop = top;
		int cuLineWidth = 0;
		int itemLeft;
		int itemTop;
		int maxHeightItem = 0;
		row = new Row();
		lineRows.clear();
		allItemFrames.clear();
		removeAllViews();
		if (getItemCount() == 0) {
			detachAndScrapAttachedViews(recycler);
			verticalScrollOffset = 0;
			return;
		}
		if (getChildCount() == 0 && state.isPreLayout()) {
			return;
		}
		detachAndScrapAttachedViews(recycler);
		if (getChildCount() == 0) {
			width = getWidth();
			height = getHeight();
			left = getPaddingLeft();
			right = getPaddingRight();
			top = getPaddingTop();
			usedMaxWidth = width - left - right;
		}

		for (int i = 0; i < getItemCount(); i++) {
			View childAt = recycler.getViewForPosition(i);
			if (View.GONE == childAt.getVisibility()) {
				continue;
			}
			measureChildWithMargins(childAt, 0, 0);
			int childWidth = getDecoratedMeasuredWidth(childAt);
			int childHeight = getDecoratedMeasuredHeight(childAt);
			if (cuLineWidth + childWidth <= usedMaxWidth) {
				itemLeft = left + cuLineWidth;
				itemTop = cuLineTop;
				Rect frame = allItemFrames.get(i);
				if (frame == null) {
					frame = new Rect();
				}
				frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
				allItemFrames.put(i, frame);
				cuLineWidth += childWidth;
				maxHeightItem = Math.max(maxHeightItem, childHeight);
				row.addViews(new Item(childHeight, childAt, frame));
				row.setCuTop(cuLineTop);
				row.setMaxHeight(maxHeightItem);
			} else {
				formatAboveRow();
				cuLineTop += maxHeightItem;
				totalHeight += maxHeightItem;
				itemTop = cuLineTop;
				itemLeft = left;
				Rect frame = allItemFrames.get(i);
				if (frame == null) {
					frame = new Rect();
				}
				frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
				allItemFrames.put(i, frame);
				cuLineWidth = childWidth;
				maxHeightItem = childHeight;
				row.addViews(new Item(childHeight, childAt, frame));
				row.setCuTop(cuLineTop);
				row.setMaxHeight(maxHeightItem);
			}
			if (i == getItemCount() - 1) {
				formatAboveRow();
				totalHeight += maxHeightItem;
			}

		}
		totalHeight = Math.max(totalHeight, getVerticalSpace());
		fillLayout(recycler, state);
	}

	private void fillLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
		if (state.isPreLayout()) {
			return;
		}

		Rect displayFrame = new Rect(getPaddingLeft(), getPaddingTop() + verticalScrollOffset,
				getWidth() - getPaddingRight(), verticalScrollOffset + (getHeight() -
                getPaddingBottom()));

		for (int j = 0; j < lineRows.size(); j++) {
			Row row = lineRows.get(j);
			float lineTop = row.cuTop;
			float lineBottom = lineTop + row.maxHeight;
			if (lineTop < displayFrame.bottom && displayFrame.top < lineBottom) {
				List<Item> views = row.views;
				for (int i = 0; i < views.size(); i++) {
					View scrap = views.get(i).view;
					measureChildWithMargins(scrap, 0, 0);
					addView(scrap);
					Rect frame = views.get(i).rect;
					layoutDecoratedWithMargins(scrap,
							frame.left,
							frame.top - verticalScrollOffset,
							frame.right,
							frame.bottom - verticalScrollOffset);
				}
			} else {
				List<Item> views = row.views;
				for (int i = 0; i < views.size(); i++) {
					View scrap = views.get(i).view;
					removeAndRecycleView(scrap, recycler);
				}
			}
		}
	}

	private void formatAboveRow() {
		List<Item> views = row.views;
		for (int i = 0; i < views.size(); i++) {
			Item item = views.get(i);
			View view = item.view;
			int position = getPosition(view);
			if (allItemFrames.get(position).top < row.cuTop + (row.maxHeight - views.get(i)
                    .useHeight) / 2) {
				Rect frame = allItemFrames.get(position);
				if (frame == null) {
					frame = new Rect();
				}
				frame.set(allItemFrames.get(position).left, (int) (row.cuTop + (row.maxHeight -
                                views.get(i).useHeight) / 2),
						allItemFrames.get(position).right, (int) (row.cuTop + (row.maxHeight -
                                views.get(i).useHeight) / 2 + getDecoratedMeasuredHeight(view)));
				allItemFrames.put(position, frame);
				item.setRect(frame);
				views.set(i, item);
			}
		}
		row.views = views;
		lineRows.add(row);
		row = new Row();
	}

	@Override
	public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
								  RecyclerView.State state) {

		int travel = dy;

		if (verticalScrollOffset + dy < 0) {
			travel = -verticalScrollOffset;//verticalScrollOffset=0
		} else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
			travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
			//verticalScrollOffset=totalHeight - getVerticalSpace()
		}

		verticalScrollOffset += travel;

		offsetChildrenVertical(-travel);
		fillLayout(recycler, state);
		return travel;
	}

	private int getVerticalSpace() {
		return self.getHeight() - self.getPaddingBottom() - self.getPaddingTop();
	}

	public int getHorizontalSpace() {
		return self.getWidth() - self.getPaddingLeft() - self.getPaddingRight();
	}

}
