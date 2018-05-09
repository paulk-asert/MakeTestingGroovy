def test(os, mem, disk) { println "$os $mem $disk" }

test('OSX', '4G', '250G')
test('Linux', '4G', '250G')
test('Win10', '4G', '250G')
test('OSX', '8G', '500G')
test('Linux', '8G', '500G')
test('Win10', '8G', '500G')
// 30 more rows

[
  ['OSX', 'Linux', 'Win10'],
  ['2G', '4G', '6G', '8G'],
  ['250G', '350G', '500G']
].combinations().each{
  os, mem, disk ->
    test(os, mem, disk)
}
