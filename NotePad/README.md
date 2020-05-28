# NotePad 张前程 116052017037 软一
## 1. 整体界面  
![image](https://github.com/huahuahuaX/AndroidTest/blob/master/NotePad/Screenshoots/%E6%95%B4%E4%BD%93.png)  

## 2. 时间戳的实现    
### 2.1 改布局  
>在原本的`Noteslist_item.xml`文件下  
>>* 插入线性布局  
>>* 增加一个TextView放置时间戳  
具体代码如下： 
```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:paddingLeft="10dp">

<TextView
    android:id="@android:id/text1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center_vertical"
    android:textSize="20dp"
    android:text="title"
    android:textStyle="bold"
    android:singleLine="true" />
    
//新增放置时间戳
<TextView
    android:id="@android:id/text2"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center_vertical"
    android:textSize="16dp"
    android:text="time"
    android:singleLine="true" />
</LinearLayout>
```  
### 2.2 增映射  
>在`NoteList.java`下  
>>* 查询条件中的`PROJECTION`增加修改时间列  
```java
private static final String[] PROJECTION = new String[] {
    NotePad.Notes._ID, // 0
    NotePad.Notes.COLUMN_NAME_TITLE, // 1
    NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, //2 新增
};
```  
>>* 创建适配器要求中`dataColumns`,`viewIDs`分别增加相对应的列和id  
```java
  // The names of the cursor columns to display in the view, initialized to the title column
  String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE
          , NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE} ; //新增

  // The view IDs that will display the cursor columns, initialized to the TextView in
  // noteslist_item.xml
  int[] viewIDs = { android.R.id.text1, android.R.id.text2 }; //新增
```  
### 2.3 换格式  
>在数据库中存储和读取的时间均为时间戳样式，因此需转换为我们所要求的`yyyy-MM-dd HH:mm:ss`格式  
>>* 使用`SimpleDateFormat`对相应列的时间进行转换  
```java
//新增 时间转换方法
private void Change(SimpleCursorAdapter adapter){
    adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if(cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_CREATE_DATE) == columnIndex  //找有时间戳的列
                    || cursor.getColumnIndex(NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE) == columnIndex){
                TextView textView = (TextView)view;

                Date date = new Date(cursor.getLong(columnIndex));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                String datetime = sdf.format(date);

                textView.setText(datetime);
                return true;
            }
            return false;
        }
    });
}
```
  
## 3. 搜索功能(按标题模糊查找)  
### 3.1 添item  
效果图：  
![image](https://github.com/huahuahuaX/AndroidTest/blob/master/NotePad/Screenshoots/%E6%95%B4%E4%BD%93.png)  
![image](https://github.com/huahuahuaX/AndroidTest/blob/master/NotePad/Screenshoots/%E6%90%9C%E7%B4%A2.png)  
>在原本的`list_options_menu.xml`文件下  
>>* 添加查询item  
```java
<!-- 新增查询   -->
<item android:id="@+id/menu_search"
      android:title=""
      android:icon="@drawable/ic_menu_search"
      android:actionViewClass="android.widget.SearchView"
      android:showAsAction="collapseActionView|ifRoom" />
```  
### 3.2 绑定事件监听以实现搜索功能  
效果图：  
![image](https://github.com/huahuahuaX/AndroidTest/blob/master/NotePad/Screenshoots/%E6%90%9C%E7%B4%A2ing.png)  
>在`NoteList.java`的`onCreateOptionsMenu`方法中  
>>* 创建item  
```java
// Inflate menu from XML resource
MenuInflater inflater = getMenuInflater();
inflater.inflate(R.menu.list_options_menu, menu);
```  
>>* 定义`SearchView`并绑定监听事件  
```java
  /*
  * 新增 search功能
  */
  final SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
  searchView.setIconifiedByDefault(true);
  searchView.setQueryHint("Search...");
  //搜索框文字变化监听
  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
          return false;
      }
      //框内文字改变
      @Override
      public boolean onQueryTextChange(String newText) {
          String seletion = NotePad.Notes.COLUMN_NAME_TITLE + " like ? ";    //设置查找条件
          String[] selectionArgs = {"%" + newText + "%"};
          Cursor cursor = managedQuery(
                  getIntent().getData(),            // Use the default content URI for the provider.
                  PROJECTION,                       // Return the note ID and title for each note.
                  seletion,                             // No where clause, return all records.
                  selectionArgs,                             // No where clause, therefore no where column values.
                  NotePad.Notes.DEFAULT_SORT_ORDER  // Use the default sort order.
          );
          // The names of the cursor columns to display in the view, initialized to the title column
          String[] dataColumns = { NotePad.Notes.COLUMN_NAME_TITLE
                  , NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE} ; //新增

          // The view IDs that will display the cursor columns, initialized to the TextView in
          // noteslist_item.xml
          int[] viewIDs = { android.R.id.text1, android.R.id.text2 }; //新增

          // Creates the backing adapter for the ListView.
          SimpleCursorAdapter adapter
                  = new SimpleCursorAdapter(
                  getApplicationContext(),          // The Context for the ListView
                  R.layout.noteslist_item,          // Points to the XML for a list item
                  cursor,                           // The cursor to get items from
                  dataColumns,
                  viewIDs
          );
          Change(adapter);    //调用时间转换的方法（详情见2.3换格式）
          setListAdapter(adapter);
          return true;
      }
  });
```
