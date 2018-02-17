package com.zacharee1.modcontrolredesign.views

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import com.zacharee1.modcontrolredesign.R
import com.zacharee1.modcontrolredesign.activities.PowerMenuActivity

class NavBarDragItemView : LinearLayout {
    val switch: Switch

    var button: PowerMenuActivity.PowerButton? = null
        set(value) {
            field = value
            switch.text = context.resources.getText(value?.name ?: R.string.blank)
            switch.isChecked = value?.enabled ?: false
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        View.inflate(context, R.layout.item_view_layout, this)
        switch = findViewById(R.id.toggle)
    }
}