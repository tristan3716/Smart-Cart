package com.exam.zzz_other_menu;

import java.util.ArrayList;

import android.content.Context;

import android.database.Cursor;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;

import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.exam.zzz_other_menu_mysql.MySQLiteHandler;
import com.example.n_mart.R;

public class Select_mart_Adapter extends BaseExpandableListAdapter {

	MySQLiteHandler handler;
	Cursor c;
	Button btn;

	// LayoutInflater�� ���������� ���ؽ�Ʈ�� �Ѱܹ޾ƾ� �Ѵ�.

	private Context context;

	// �� �׷쿡 ������ ����Ʈ 

	private ArrayList<Select_mart_GM> groups; 


	// �� �׷��� �������� �����ִ� �ڽ� ����Ʈ

	private ArrayList<ArrayList<Select_mart_Model>> children;

	// xml���� ������ UI�� ������ �ش�.

	private LayoutInflater inflater;

	public Select_mart_Adapter(Context context, ArrayList<Select_mart_GM> gropus, ArrayList<ArrayList<Select_mart_Model>> children)
	{
		this.groups = gropus;
		this.children = children;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	public ArrayList<ArrayList<Select_mart_Model>> getAllList()
	{
		return this.children;
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public Select_mart_Model getChild(int groupPosition, int childPosition)
	{
		return children.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return children.get(groupPosition).get(childPosition).getId();
	}

	// children�� �����ش�. ArrayAdapter�� getView�� �����ϰ� ó���ϸ� �ȴ�.

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		final Select_mart_Model model = getChild(groupPosition, childPosition);
		View view = convertView;

		if (view == null)
		{
			view = inflater.inflate(R.layout.select_mart_child_row, null);
		}

		if (model != null)
		{
			handler = MySQLiteHandler.open(context);

			ImageView img = (ImageView) view.findViewById(R.id.Select_mart_imageView1);
			TextView childName = (TextView) view.findViewById(R.id.Select_mart_childname);
			TextView childAge = (TextView) view.findViewById(R.id.Select_mart_rgb);
			btn = (Button) view.findViewById(R.id.Select_mart_button1);

			btn.setFocusable(false);

			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					c = handler.selectbookmarker(model.getMartName());
					c.moveToFirst();

					if(c.getInt(2)==0){
						handler.updatebookmarker(model.getMartName(), 1);
						model.setBookMarker(1);
						notifyDataSetChanged();
						Toast.makeText(context, "���ã�⿡ �߰��Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
					} else {
						handler.updatebookmarker(model.getMartName(), 0);
						model.setBookMarker(0);
						notifyDataSetChanged();
						Toast.makeText(context, "���ã�⿡�� ��ü�Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
					}

				}
			});

			if(model.getBookMarker()==0){
				btn.setBackgroundResource(R.drawable.book_no);
				notifyDataSetChanged();
			} else {
				btn.setBackgroundResource(R.drawable.book_yes);
				notifyDataSetChanged();
			}

			String divword = "";
			String divword2 = "";

			if (model.getMartName().contains("�̸�Ʈ")) {

				divword = model.getMartName().substring(0, 4);
				divword2 = model.getMartName().substring(4);

			} else if (model.getMartName().contains("�Ե���Ʈ")) {

				divword = model.getMartName().substring(0, 5);
				divword2 = model.getMartName().substring(5);

			} else if (model.getMartName().contains("Ȩ�÷���")) {

				divword = model.getMartName().substring(0, 5);
				divword2 = model.getMartName().substring(5);

			} else if (model.getMartName().contains("�ϳ��θ�Ʈ")) {

				divword = model.getMartName().substring(0, 6);
				divword2 = model.getMartName().substring(6);

			} else if (model.getMartName().contains("�ż����ȭ��")) {

				divword = model.getMartName().substring(0, 7);
				divword2 = model.getMartName().substring(7);
			} else if (model.getMartName().contains("�����ȭ��")) {

				divword = model.getMartName().substring(0, 6);
				divword2 = model.getMartName().substring(6);

			} else if (model.getMartName().contains("�Ե���ȭ��")) {

				divword = model.getMartName().substring(0, 6);
				divword2 = model.getMartName().substring(6);
			}

			childName.setText(divword);
			childAge.setText(divword2);

			if (model.getMartName().contains("�Ե���Ʈ ���￪��")) {
				img.setImageResource(R.drawable.l_seoulstation);
			}else if (model.getMartName().contains("�Ե���Ʈ ������")){
				img.setImageResource(R.drawable.l_gangbyun2);
			}else if (model.getMartName().contains("�Ե���Ʈ ��õ��")) {
				img.setImageResource(R.drawable.l_keumchon);
			}else if (model.getMartName().contains("Ȩ�÷��� �����")) {
				img.setImageResource(R.drawable.h_jamsil);
			}else if (model.getMartName().contains("Ȩ�÷��� ������")) {
				img.setImageResource(R.drawable.h_gangdong);
			}else if (model.getMartName().contains("Ȩ�÷��� ������")) {
				img.setImageResource(R.drawable.h_banghak);
			}else if (model.getMartName().contains("Ȩ�÷��� ���빮��")) {
				img.setImageResource(R.drawable.h_dongdaemoon);
			}else if (model.getMartName().contains("Ȩ�÷��� �����")) {
				img.setImageResource(R.drawable.h_myunmok);
			}else if (model.getMartName().contains("Ȩ�÷��� ��������")) {
				img.setImageResource(R.drawable.h_youngdeungpo);
			}else if (model.getMartName().contains("Ȩ�÷��� ������")) {
				img.setImageResource(R.drawable.h_siheung);
			}else if (model.getMartName().contains("�̸�Ʈ ������")) {
				img.setImageResource(R.drawable.e_sungsoo);
			}else if (model.getMartName().contains("�̸�Ʈ ������")) {
				img.setImageResource(R.drawable.e_eunpyung);
			}else if (model.getMartName().contains("�̸�Ʈ ����")) {
				img.setImageResource(R.drawable.e_mokdong);
			}else if (model.getMartName().contains("�̸�Ʈ �����")) {
				img.setImageResource(R.drawable.e_yongsan);
			}else if (model.getMartName().contains("�̸�Ʈ û����")) {
				img.setImageResource(R.drawable.e_chungye);
			}else if (model.getMartName().contains("�̸�Ʈ ������")) {
				img.setImageResource(R.drawable.e_myungil);
			}else if (model.getMartName().contains("�̸�Ʈ �ŵ�����")) {
				img.setImageResource(R.drawable.e_sindorim);
			}else if (model.getMartName().contains("�̸�Ʈ â����")) {
				img.setImageResource(R.drawable.e_changdong);
			}else if (model.getMartName().contains("�̸�Ʈ �ھ���")) {
				img.setImageResource(R.drawable.e_jayang);
			}else if (model.getMartName().contains("�̸�Ʈ �����")) {
				img.setImageResource(R.drawable.e_sangbong);
			}else if (model.getMartName().contains("�̸�Ʈ �̾���")) {
				img.setImageResource(R.drawable.e_mia);
			}else if (model.getMartName().contains("�̸�Ʈ ������")) {
				img.setImageResource(R.drawable.e_gayang);
			}else if (model.getMartName().contains("�̸�Ʈ ���ǵ���")) {
				img.setImageResource(R.drawable.e_yuido);
			}else if (model.getMartName().contains("�̸�Ʈ �սʸ���")) {
				img.setImageResource(R.drawable.e_wangsiri);
			}else if (model.getMartName().contains("�ϳ��θ�Ʈ ����")) {
				img.setImageResource(R.drawable.n_mokdong);
			}else if (model.getMartName().contains("�ϳ��θ�Ʈ �̾���")) {
				img.setImageResource(R.drawable.n_mia);
			}else if (model.getMartName().contains("�ϳ��θ�Ʈ ������")) {
				img.setImageResource(R.drawable.n_yangjae);
			}else if (model.getMartName().contains("�ϳ��θ�Ʈ �����")) {
				img.setImageResource(R.drawable.n_yongsan);
			}else if (model.getMartName().contains("�ż����ȭ�� ����")) {
				img.setImageResource(R.drawable.s_bon);
			}else if (model.getMartName().contains("�ż����ȭ�� ������")) {
				img.setImageResource(R.drawable.s_gangnam);
			}else if (model.getMartName().contains("�Ե���ȭ�� ����")) {
				img.setImageResource(R.drawable.ld_bon);
			}else if (model.getMartName().contains("�Ե���ȭ�� û������")) {
				img.setImageResource(R.drawable.ld_chungryangri);
			}else if (model.getMartName().contains("�Ե���ȭ�� ������")) {
				img.setImageResource(R.drawable.ld_gangnam);
			}else if (model.getMartName().contains("�Ե���ȭ�� �����")) {
				img.setImageResource(R.drawable.ld_jamsil);
			}else if (model.getMartName().contains("�Ե���ȭ�� ������")) {
				img.setImageResource(R.drawable.ld_khwanak);
			}else if (model.getMartName().contains("�Ե���ȭ�� �̾���")) {
				img.setImageResource(R.drawable.ld_mia);
			}else if (model.getMartName().contains("�Ե���ȭ�� �����")) {
				img.setImageResource(R.drawable.ld_nowon);
			}else if (model.getMartName().contains("�Ե���ȭ�� ��������")) {
				img.setImageResource(R.drawable.ld_youngdeungpo);
			}else if (model.getMartName().contains("�����ȭ�� �̾���")) {
				img.setImageResource(R.drawable.hd_mia);
			}else if (model.getMartName().contains("�����ȭ�� ������")) {
				img.setImageResource(R.drawable.hd_sinchon);
			}
		}
		handler.close();

		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return children.get(groupPosition).size();
	}

	@Override
	public String getGroup(int groupPosition)
	{
		return groups.get(groupPosition).getName();
	}

	@Override
	public int getGroupCount()
	{
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	// ��׷��� �����ش�. ArrayAdapter�� getView�� �����ϰ� ó���ϸ� �ȴ�.
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		View view = convertView;
		String group = (String) getGroup(groupPosition);
		if (view == null)
		{
			view = inflater.inflate(R.layout.group_row, null);
		}

		ImageView img = (ImageView) view.findViewById(R.id.Select_mart_imageView2);

		if (group.contains("�Ե���Ʈ")) {
			img.setImageResource(R.drawable.l_logodown);
		} else if (group.contains("�̸�Ʈ")) {
			img.setImageResource(R.drawable.e_logodown);
		} else if (group.contains("Ȩ�÷���")) {
			img.setImageResource(R.drawable.h_logodown);
		} else if (group.contains("�ϳ��θ�Ʈ")) {
			img.setImageResource(R.drawable.n_logodown);
		} else if (group.contains("�ż����ȭ��")) {
			img.setImageResource(R.drawable.s_logodown);
		} else if (group.contains("�Ե���ȭ��")) {
			img.setImageResource(R.drawable.ld_logodown);
		} else if (group.contains("�����ȭ��")) {
			img.setImageResource(R.drawable.hd_logodown);
		}

		if(isExpanded){
			if (group.contains("�Ե���Ʈ")) {
				img.setImageResource(R.drawable.l_logoup);
			} else if (group.contains("�̸�Ʈ")) {
				img.setImageResource(R.drawable.e_logoup);
			} else if (group.contains("Ȩ�÷���")) {
				img.setImageResource(R.drawable.h_logoup);
			} else if (group.contains("�ϳ��θ�Ʈ")) {
				img.setImageResource(R.drawable.n_logoup);
			} else if (group.contains("�ż����ȭ��")) {
				img.setImageResource(R.drawable.s_logoup);
			} else if (group.contains("�Ե���ȭ��")) {
				img.setImageResource(R.drawable.ld_logoup);
			} else if (group.contains("�����ȭ��")) {
				img.setImageResource(R.drawable.hd_logoup);
			}
		}

		return view;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
}