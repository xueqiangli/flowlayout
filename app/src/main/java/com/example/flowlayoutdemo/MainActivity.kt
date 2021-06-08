package com.example.flowlayoutdemo


import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    //标签名称
    private val mTvNames = arrayOf( "这是顶部","钉宫理惠", "灼眼的夏娜"
        , "绯弹的亚里亚", "零之使魔", "夕阳染红的街道","流式布局测试一下下，一行可以能放一个牙","短行1","短行2","短行3","短行4",
        "超过屏幕的一行行高，你觉着怎么布局呢，说说你的想法","动漫", "钉宫理惠", "灼眼的夏娜"
        , "绯弹的亚里亚", "零之使魔", "夕阳染红的街道","流式布局测试一下下，一行可以能放一个牙","短行1","短行2","短行3","短行4",
        "超过屏幕的一行行高，你觉着怎么布局呢，说说你的想法","动漫", "钉宫理惠", "灼眼的夏娜"
        , "绯弹的亚里亚", "零之使魔", "夕阳染红的街道","流式布局测试一下下，一行可以能放一个牙","短行1","短行2","短行3","短行4",
        "超过屏幕的一行行高，你觉着怎么布局呢，说说你的想法","动漫", "钉宫理惠", "灼眼的夏娜"
        , "绯弹的亚里亚", "零之使魔", "夕阳染红的街道","流式布局测试一下下，一行可以能放一个牙","短行1","短行2","短行3","短行4",
        "超过屏幕的一行行高，你觉着怎么布局呢，说说你的想法","动漫", "钉宫理惠", "灼眼的夏娜"
        , "绯弹的亚里亚", "零之使魔", "夕阳染红的街道","流式布局测试一下下，一行可以能放一个牙","短行1","短行2","短行3","短行4","超过屏幕的一行行高，你觉着怎么布局呢，说说你的想法11","动漫", "钉宫理惠", "灼眼的夏娜"
        , "绯弹的亚里亚", "零之使魔", "夕阳染红的街道","流式布局测试一下下，一行可以能放一个牙","短行1","短行2","短行3","短行4","超过屏幕的一行行高，你觉着怎么布局呢，说说你的想法12","动漫","这是底部")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        btn.setOnClickListener {
            var s=edit.text.toString()
            addTextView(s)
        }

    }


    fun init() {
        //遍历标签名称数组
        for (s in mTvNames) {
            addTextView(s)
        }
    }

    fun addTextView(tvName: String?) {
        //加载TextView并设置名称，并设置名称
        val tv = LayoutInflater.from(this).inflate(R.layout.tv, flow_layout, false) as TextView
        tv.text = tvName
        //把TextView加入流式布局
        flow_layout.addView(tv)
    }
}