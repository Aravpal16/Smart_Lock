// Generated code from Butter Knife. Do not modify!
package com.example.philipgo.servodoorlock;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Data_ViewBinding implements Unbinder {
  private Data target;

  private View view7f080087;

  @UiThread
  public Data_ViewBinding(Data target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Data_ViewBinding(final Data target, View source) {
    this.target = target;

    View view;
    target.doubleText = Utils.findRequiredViewAsType(source, R.id.doubleText, "field 'doubleText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.doubleLayout, "method 'doubleClicked'");
    view7f080087 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.doubleClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Data target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.doubleText = null;

    view7f080087.setOnClickListener(null);
    view7f080087 = null;
  }
}
