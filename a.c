private void loadNotes() {
    notesList.clear(); // 清空当前的笔记列表
    Cursor cursor = db.query("notes", null, "username=?", new String[]{username}, null, null, "timestamp DESC");
    if (cursor != null) {
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title")); // 获取标题
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content")); // 获取内容

            // 将内容按换行符分割，并取第一行内容
            String firstLine = content.split("\n")[0];

            // 将标题和截取后的第一行内容添加到笔记列表中
            notesList.add(title + "\n" + firstLine);

        }
        cursor.close(); // 关闭游标
    }
    adapter.notifyDataSetChanged(); // 通知适配器数据已更改
}


// 布局文件
<TextView
    android:id="@+id/noteContent"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:maxLines="1"
    android:ellipsize="end" />
