package com.project.gestureappv2;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class ImageActivity extends ActionBarActivity {

	private final static int SELECT_IMAGE = 1;
	private static int currentSelection = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new DashboardFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class DashboardFragment extends Fragment {

		public DashboardFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_dashboard,
					container, false);

			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
			gridview.setAdapter(new GridItemsAdapter(rootView.getContext()));

			((GridItemsAdapter) gridview.getAdapter()).loadAllImages();

			gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					if (position > AdapterView.INVALID_POSITION) {

						currentSelection = position;

						int count = parent.getChildCount();
						for (int i = 0; i < count; i++) {
							View curr = parent.getChildAt(i);

							curr.setOnDragListener(new View.OnDragListener() {

								@Override
								public boolean onDrag(View v, DragEvent event) {

									boolean result = true;
									int action = event.getAction();
									switch (action) {
									case DragEvent.ACTION_DRAG_STARTED:
										break;
									case DragEvent.ACTION_DRAG_LOCATION:
										break;
									case DragEvent.ACTION_DRAG_ENTERED:
										// v.setBackgroundResource(R.drawable.shape_image_view_small_gallery_selected);
										break;
									case DragEvent.ACTION_DRAG_EXITED:
										// v.setBackgroundResource(R.drawable.shape_image_view_small_gallery_unselected);
										break;
									case DragEvent.ACTION_DROP:
										if (event.getLocalState() == v) {
											result = false;
										} else {
											View droped = (View) event
													.getLocalState();
											int dropItemPos = ((int) droped
													.getTag());

											GridView parent = (GridView) droped
													.getParent();
											GridItemsAdapter adapter = (GridItemsAdapter) parent
													.getAdapter();

											View target = v;
											int targetItemPos = (int) target
													.getTag();
											// Update database to swap image
											// positions
											DatabaseHandler dh = DatabaseHandler
													.getInstance(getActivity());
											int dropID = dh
													.getIDofPos(dropItemPos);
											int targID = dh
													.getIDofPos(targetItemPos);
											dh.updateImagePos(dropID,
													targetItemPos);
											dh.updateImagePos(targID,
													dropItemPos);

											Bitmap drpImage = (Bitmap) adapter
													.getItem(dropItemPos);
											Bitmap trgImage = (Bitmap) adapter
													.getItem(targetItemPos);
											adapter.removeItemAt(dropItemPos);
											adapter.setItemAt(dropItemPos,
													trgImage);
											adapter.removeItemAt(targetItemPos);
											adapter.setItemAt(targetItemPos,
													drpImage);
											// adapter.loadAllImages();
											adapter.notifyDataSetChanged();

										}
										break;
									case DragEvent.ACTION_DRAG_ENDED:
										// v.setBackgroundResource(R.drawable.shape_image_view_small_gallery_unselected);
										break;
									default:
										result = false;
										break;
									}
									return result;
								}
							});
						}

						int relativePosition = position
								- parent.getFirstVisiblePosition();

						View target = (View) parent
								.getChildAt(relativePosition);

						ClipData data = ClipData.newPlainText("DragData",
								"Checking");
						target.startDrag(data, new View.DragShadowBuilder(
								target), target, 0);
					}

					return false;
				}
			});

			// Image Show
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					try {
						
						//DatabaseHandler dh = DatabaseHandler.getInstance(getActivity());
						//byte[] imgByte= dh.getImage(position);
						//Bitmap image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
						
						//Toast.makeText(getActivity(), "position " + position,Toast.LENGTH_SHORT).show();		
						
						GridItemsAdapter adapter = (GridItemsAdapter) parent.getAdapter();
						ParameterPasser.image = (Bitmap)adapter.getItem(position);
					
						Intent i = new Intent(getActivity(),
								SingleImageActivity.class);
						//Bundle extras = new Bundle();						
						//extras.putParcelable("image", image);
						//i.putExtras(extras);
						startActivity(i);

					} catch (Exception e) {

					}

				}

			});

			Button bAddImage = (Button) rootView.findViewById(R.id.add_image);
			bAddImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// in onCreate or any event where your want the user to
					// select a file
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							SELECT_IMAGE);

				}

			});

			Button bRemoveImage = (Button) rootView
					.findViewById(R.id.remove_image);
			bRemoveImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (currentSelection > -1) {
						// Removes currently selected Image
						DatabaseHandler dh = DatabaseHandler
								.getInstance(getActivity());
						dh.deleteImage(currentSelection);
						// update all other image position
						dh.updateImagePos(currentSelection);

						GridView gv = (GridView) (getActivity()
								.findViewById(R.id.gridview));
						GridItemsAdapter gia = (GridItemsAdapter) gv
								.getAdapter();
						gia.loadAllImages();
						gia.notifyDataSetChanged();
						currentSelection = -1;
					}
				}

			});

			return rootView;
		}

		// UPDATED
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// Toast.makeText(getActivity(), "Returned",
			// Toast.LENGTH_SHORT).show();
			if (resultCode == RESULT_OK) {
				if (requestCode == SELECT_IMAGE) {
					Uri selectedImageUri = data.getData();
					String selectedImagePath;
					String filemanagerstring;
					// OI FILE Manager
					filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					selectedImagePath = getPath(selectedImageUri);

					// NOW WE HAVE OUR WANTED STRING
					if (selectedImagePath != null) {
						// Toast.makeText(getActivity(), selectedImagePath,
						// Toast.LENGTH_SHORT).show();
						// System.out.println("selectedImagePath is the right one for you!");
						saveImage(selectedImagePath);
					} else {
						// Toast.makeText(getActivity(), filemanagerstring,
						// Toast.LENGTH_SHORT).show();
						// System.out.println("filemanagerstring is the right one for you!");
						saveImage(filemanagerstring);
					}

					// Update it on Screen
					GridView gridview = (GridView) getActivity().findViewById(
							R.id.gridview);
					GridItemsAdapter gAdapter = (GridItemsAdapter) gridview
							.getAdapter();
					gAdapter.loadAllImages();
					gAdapter.notifyDataSetChanged();
				}
			}
		}

		private void saveImage(String imagePath) {
			File imgFile = new File(imagePath);

			if (imgFile.exists()) {

				Bitmap bitmap = BitmapFactory.decodeFile(imgFile
						.getAbsolutePath());
				byte[] imageBytes = getBitmapAsByteArray(bitmap);
				DatabaseHandler dh = DatabaseHandler.getInstance(getActivity());
				int pos = dh.getImagesCount();
				dh.addImage(imageBytes, pos);
			}
		}

		public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0, outputStream);
			return outputStream.toByteArray();
		}

		// UPDATED!
		public String getPath(Uri uri) {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = getActivity().getContentResolver().query(uri,
					projection, null, null, null);
			if (cursor != null) {
				// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
				// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE
				// MEDIA
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			} else
				return null;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Propagating to Fragment
		// Toast.makeText(this, "Reached",
		// Toast.LENGTH_SHORT).show();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
