package com.example.brvdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.models
import com.drake.brv.utils.page
import com.drake.brv.utils.setup
import com.drake.statelayout.StateConfig
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { _, _ -> MaterialHeader(application) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { _, _ -> ClassicsFooter(application) }

        findViewById<RecyclerView>(R.id.recycler).setup {
            addType<Item>(R.layout.item_main)

            onCreate {
                findView<RecyclerView>(R.id.recycler).setup {
                    addType<InnerItem>(R.layout.item_main_inner)

                    onBind {
                        findView<TextView>(R.id.text).text = getModel<InnerItem>().id
                    }
                }
            }

            onBind {
                findView<RecyclerView>(R.id.recycler).models = getModel<Item>().innerItemList
            }
        }

        //内部recyclerview的item超过一屏，开启分页加载数据，出现白屏bug
//        findViewById<RecyclerView>(R.id.recycler).page(loadMoreEnabled = false).apply {
//            onRefresh { addData(fakeData()) }
//            refreshing()
//        }
        //不开启分页设置数据，正常显示
        findViewById<RecyclerView>(R.id.recycler).models = fakeData()
    }

    private fun fakeData(): List<Item> = Array(10) { i ->
        Item(i.toString(), Array(10 - i) { j -> InnerItem("$i, $j") }.toList())
    }.toList()
}

data class Item(val id: String, val innerItemList: List<InnerItem>)

data class InnerItem(val id: String)