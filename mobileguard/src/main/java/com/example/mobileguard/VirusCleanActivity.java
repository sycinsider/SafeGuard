package com.example.mobileguard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mobileguard.bean.VirusBean;
import com.github.lzyzsd.circleprogress.ArcProgress;

import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VirusCleanActivity extends Activity implements View.OnClickListener {

    private RelativeLayout actAntiVirusRlScanning;
    private ArcProgress actAntiVirusPb;
    private TextView actAntiVirusTvScanningPkg;
    private LinearLayout actAntiVirusLlScanned;
    private TextView actAntiVirusTvResult;
    private LinearLayout actAntiVirusLlAnim;
    private ImageView actAntiVirusIvLeft;
    private ImageView actAntiVirusIvRight;
    private ListView actAntiVirusLv;
    private ArrayList<VirusBean> datas;
    private PackageManager pm;
    private VirusCleanItemAdapter adapter;
    private VirusTask task;
    private int widthLeft;
    private int widthRight;
    private Button actAntiVirusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_clean);

        initView();

    }

    private boolean isFocused = false;

    @Override
    protected void onStart() {
        super.onStart();
        isFocused = true;
        initData();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isFocused = false;
    }

    private void initData() {
        pm = getPackageManager();
        task = new VirusTask();
        task.execute();
    }


    class VirusTask extends AsyncTask<Void, VirusBean, Void> {

        private int progress;
        private int max;
        private int virusNum;

        @Override
        protected void onPreExecute() {
            datas = new ArrayList<>();
            adapter = new VirusCleanItemAdapter(VirusCleanActivity.this);
            actAntiVirusLv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            actAntiVirusRlScanning.setVisibility(View.VISIBLE);
            actAntiVirusLlScanned.setVisibility(View.GONE);
            actAntiVirusLlAnim.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<ApplicationInfo> list = pm.getInstalledApplications(0);
            max = list.size();
            Random random = new Random();
            for (ApplicationInfo info :
                    list) {
                if (!isFocused) {
                    break;
                }
                VirusBean bean = new VirusBean();
                bean.name = pm.getApplicationLabel(info).toString();
                bean.pkgName = info.packageName;
                bean.icon = pm.getApplicationIcon(info);
                if (random.nextInt() % 2 == 0) {
                    bean.isVirus = true;
                } else {
                    bean.isVirus = false;
                }
                publishProgress(bean);
                SystemClock.sleep(100);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(VirusBean... values) {
            progress++;
            actAntiVirusPb.setProgress((int) (progress * 100f / max + 0.5f));
            VirusBean bean = values[0];
            if (bean.isVirus) {
                datas.add(0, bean);
                virusNum++;
            } else {
                datas.add(bean);
            }
            if (virusNum > 0 && virusNum < 10) {
                actAntiVirusPb.setFinishedStrokeColor(Color.YELLOW);

            } else if (virusNum >= 10) {
                actAntiVirusPb.setFinishedStrokeColor(Color.RED);

            }
            actAntiVirusTvScanningPkg.setText(bean.pkgName);
            adapter.notifyDataSetChanged();
            actAntiVirusLv.setSelection(adapter.getCount() - 1);
        }

        @Override
        protected void onPostExecute(Void result) {
            actAntiVirusLv.smoothScrollToPosition(0);
            actAntiVirusRlScanning.setVisibility(View.GONE);
            actAntiVirusLlScanned.setVisibility(View.VISIBLE);
            actAntiVirusLlAnim.setVisibility(View.VISIBLE);
            if (virusNum > 0) {
                String string = "您的手机有" + virusNum + "个病毒";
                int end = string.indexOf("个");

                SpannableString ss = new SpannableString(string);
                ss.setSpan(new ForegroundColorSpan(Color.RED), 5, end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                actAntiVirusTvResult.setText(ss);
            } else {

                actAntiVirusTvResult.setText("您的手机很安全");
            }

            playOpenAnim();

        }

        private void playOpenAnim() {
            actAntiVirusRlScanning.setDrawingCacheEnabled(true);
            actAntiVirusRlScanning.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap drawingCache =
                    actAntiVirusRlScanning.getDrawingCache();
            actAntiVirusIvLeft.setImageBitmap(getLeft(drawingCache));
            actAntiVirusIvRight.setImageBitmap(getRight(drawingCache));
            actAntiVirusIvLeft.measure(0, 0);
            actAntiVirusIvRight.measure(0, 0);
            widthLeft = actAntiVirusIvLeft.getMeasuredWidth();
            widthRight = actAntiVirusIvRight.getMeasuredWidth();
            ObjectAnimator left1 = ObjectAnimator.ofFloat(actAntiVirusIvLeft, "alpha", 1.0f, 0.0f);
            ObjectAnimator left2 = ObjectAnimator.ofFloat(actAntiVirusIvLeft, "translationX", 0, -widthLeft);
            ObjectAnimator right1 = ObjectAnimator.ofFloat(actAntiVirusIvRight, "alpha", 1.0f, 0.0f);
            ObjectAnimator animator = ObjectAnimator.ofFloat(actAntiVirusLlScanned, "alpha", 0.0f, 1.0f);
            ObjectAnimator right2 = ObjectAnimator.ofFloat(actAntiVirusIvRight, "translationX", 0, widthLeft);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(left1, left2, right1, right2, animator);
            set.setDuration(1500);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    actAntiVirusBtn.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    actAntiVirusBtn.setEnabled(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        }

        private void playCloseAnim() {
            ObjectAnimator left1 = ObjectAnimator.ofFloat(actAntiVirusIvLeft, "alpha", 0.0f, 1.0f);
            ObjectAnimator left2 = ObjectAnimator.ofFloat(actAntiVirusIvLeft, "translationX", -widthLeft, 0);
            ObjectAnimator right1 = ObjectAnimator.ofFloat(actAntiVirusIvRight, "alpha", 0.0f, 1.0f);
            ObjectAnimator animator = ObjectAnimator.ofFloat(actAntiVirusLlScanned, "alpha", 1.0f, 0.0f);
            ObjectAnimator right2 = ObjectAnimator.ofFloat(actAntiVirusIvRight, "translationX", widthLeft, 0);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(left1, left2, right1, right2, animator);
            set.setDuration(1500);
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    actAntiVirusBtn.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    actAntiVirusBtn.setEnabled(true);
                    initData();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        }

        private Bitmap getLeft(Bitmap drawingCache) {
            int width = (int) (drawingCache.getWidth() / 2f + 0.5f);
            int height = drawingCache.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, drawingCache.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            Matrix matrix = new Matrix();
            canvas.drawBitmap(drawingCache, matrix, paint);
            return bitmap;
        }

        private Bitmap getRight(Bitmap drawingCache) {
            int width = (int) (drawingCache.getWidth() / 2f + 0.5f);
            int height = drawingCache.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, drawingCache.getConfig());
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            Matrix matrix = new Matrix();
            matrix.setTranslate(-width, 0);
            canvas.drawBitmap(drawingCache, matrix, paint);
            return bitmap;
        }
    }

    private void initView() {
        actAntiVirusRlScanning = (RelativeLayout) findViewById(R.id.act_anti_virus_rl_scanning);
        actAntiVirusPb = (ArcProgress) findViewById(R.id.act_anti_virus_pb);
        actAntiVirusTvScanningPkg = (TextView) findViewById(R.id.act_anti_virus_tv_scanning_pkg);
        actAntiVirusLlScanned = (LinearLayout) findViewById(R.id.act_anti_virus_ll_scanned);
        actAntiVirusTvResult = (TextView) findViewById(R.id.act_anti_virus_tv_result);
        actAntiVirusBtn = (Button) findViewById(R.id.act_anti_virus_btn_rescan);
        actAntiVirusBtn.setOnClickListener(this);
        actAntiVirusLlAnim = (LinearLayout) findViewById(R.id.act_anti_virus_ll_anim);
        actAntiVirusIvLeft = (ImageView) findViewById(R.id.act_anti_virus_iv_left);
        actAntiVirusIvRight = (ImageView) findViewById(R.id.act_anti_virus_iv_right);
        actAntiVirusLv = (ListView) findViewById(R.id.act_anti_virus_lv);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.act_anti_virus_btn_rescan:
                task.playCloseAnim();
                break;
        }
    }

    public class VirusCleanItemAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public VirusCleanItemAdapter(Context context) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public VirusBean getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.virus_clean_item, null);
                convertView.setTag(new ViewHolder(convertView));
            }
            initializeViews((VirusBean) getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(VirusBean bean, ViewHolder holder) {
            holder.itemVirusIcon.setImageDrawable(bean.icon);
            holder.itemVirusTvName.setText(bean.name);
            if (bean.isVirus) {
                holder.itemVirusTvIsVirus.setText("高度威胁");
                holder.itemVirusTvIsVirus.setTextColor(Color.RED);
                holder.itemVirusIvDel.setVisibility(View.VISIBLE);
            } else {
                holder.itemVirusTvIsVirus.setText("安全");
                holder.itemVirusTvIsVirus.setTextColor(Color.GREEN);
                holder.itemVirusIvDel.setVisibility(View.GONE);
            }
        }

        protected class ViewHolder {
            private ImageView itemVirusIcon;
            private TextView itemVirusTvName;
            private TextView itemVirusTvIsVirus;
            private ImageView itemVirusIvDel;

            public ViewHolder(View view) {
                itemVirusIcon = (ImageView) view.findViewById(R.id.item_virus_icon);
                itemVirusTvName = (TextView) view.findViewById(R.id.item_virus_tv_name);
                itemVirusTvIsVirus = (TextView) view.findViewById(R.id.item_virus_tv_isVirus);
                itemVirusIvDel = (ImageView) view.findViewById(R.id.item_virus_iv_del);
            }
        }
    }

}
