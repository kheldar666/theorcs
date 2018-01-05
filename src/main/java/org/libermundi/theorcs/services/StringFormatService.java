package org.libermundi.theorcs.services;

import java.util.Date;

public interface StringFormatService {

    static final String LOREM_IPSUM="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc cursus dolor id posuere laoreet. Integer gravida lectus nunc, aliquam ornare felis lobortis non. Fusce pretium quis ipsum condimentum luctus. Nam nec eros a massa feugiat molestie. Aliquam nec tincidunt lorem. Nam in elit sit amet erat commodo pulvinar quis at purus. Aenean id pharetra nunc, at suscipit mauris. Suspendisse vel rhoncus lacus, non fringilla nisl.\n" +
                    "\n" +
                    "Praesent facilisis justo vitae tellus laoreet vulputate. Sed volutpat elementum lacus, et sollicitudin justo feugiat vitae. Pellentesque dictum suscipit eros at placerat. Integer eget facilisis nisl. Proin porttitor ante vitae leo aliquet ornare. Curabitur mollis nisl vel lectus accumsan, quis finibus est vulputate. Cras vitae venenatis quam, eget vehicula nulla. Sed hendrerit mauris in magna accumsan, a porttitor nisl tincidunt. Duis non convallis dolor. Duis mollis lorem eu rhoncus mollis. Suspendisse interdum felis vel laoreet faucibus.\n" +
                    "\n" +
                    "Curabitur dapibus rhoncus elit, vitae maximus enim luctus eget. Praesent sem purus, vehicula a sollicitudin vitae, molestie eget nisi. Vestibulum vitae venenatis magna. Curabitur eget sapien lacus. Suspendisse id sapien vitae lorem ultricies efficitur. Sed venenatis, ligula vel aliquam luctus, leo augue accumsan ligula, eget egestas ligula risus molestie diam. Vestibulum eu nunc est. In hac habitasse platea dictumst.\n" +
                    "\n" +
                    "Morbi mollis maximus turpis eu accumsan. Nulla vel sollicitudin sapien. Integer tempor finibus lorem et elementum. Donec maximus cursus massa, ut hendrerit enim pharetra a. Phasellus nisi nisl, accumsan vitae varius et, maximus nec nunc. Quisque lacinia ipsum at iaculis dignissim. Morbi non aliquam magna, eget cursus purus. Pellentesque vel leo vestibulum, suscipit tortor vel, pretium est. Nullam id ultrices sem, vestibulum dictum ipsum. Suspendisse lectus felis, ornare nec elit et, feugiat convallis nibh.\n" +
                    "\n" +
                    "Sed aliquam quam urna, ut venenatis ex aliquam ut. Curabitur tempus sodales faucibus. Nam condimentum neque nec sem sodales aliquet. Integer sit amet tellus a eros congue congue et et urna. Quisque suscipit interdum tellus bibendum posuere. Quisque pulvinar massa eu enim condimentum congue. Donec a hendrerit odio. Sed rutrum varius urna eget mattis. Etiam vestibulum risus sit amet odio vestibulum commodo. Ut vehicula, lacus ac malesuada condimentum, urna dui elementum eros, quis accumsan dui justo sed tortor. Vivamus convallis convallis augue, et fermentum mi luctus et. Ut in fringilla mi. Maecenas eleifend nisi ut erat sodales pretium. ";

    String formatDate(Date date);
}
