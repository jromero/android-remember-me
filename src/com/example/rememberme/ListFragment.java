package com.example.rememberme;

import java.util.ArrayList;
import java.util.List;

import com.example.rememberme.data.Person;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListFragment extends Fragment {

	private ListView mListView;
	private ArrayList<Person> mPeople;
	private PersonAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		mListView = (ListView) view.findViewById(R.id.list_view);

		mPeople = new ArrayList<Person>();
		mPeople.add(new Person("John", "Wow", null));
		mPeople.add(new Person("Jimmy", "Great", null));
		mPeople.add(new Person("Jerry", "Guy", null));
		mPeople.add(new Person("Greg", "What", null));
		mPeople.add(new Person("Bill", "", null));
		mPeople.add(new Person("Bob", "", null));
		mPeople.add(new Person("Brian", "", null));
		mPeople.add(new Person("Bow", "", null));
		mPeople.add(new Person("Tom", "", null));
		mPeople.add(new Person("Lucy", "", null));
		mPeople.add(new Person("Spock", "", null));

		mAdapter = new PersonAdapter(getActivity(), mPeople);
		mListView.setAdapter(mAdapter);

		return view;
	}

	public static class PersonAdapter extends BaseAdapter {

		private Context mContext;
		private List<Person> mPeople;
		private LayoutInflater mLayoutInflater;

		public PersonAdapter(Context context, List<Person> people) {
			mContext = context;
			mPeople = people;
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mPeople.size();
		}

		@Override
		public Object getItem(int position) {
			return mPeople.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = mLayoutInflater.inflate(R.layout.item_person, null);

			TextView nameTextView = (TextView) view.findViewById(R.id.txt_name);
			TextView notesTextView = (TextView) view
					.findViewById(R.id.txt_notes);

			Person person = (Person) getItem(position);

			nameTextView.setText(person.getName());
			notesTextView.setText(person.getNotes());

			return view;
		}
	}
}
