def test(os, mem, disk) { /* ... */ }

test('Lion', '4G', '250G')
test('Linux', '4G', '250G')
test('Win8', '4G', '250G')
test('Lion', '8G', '500G')
test('Linux', '8G', '500G')
test('Win8', '8G', '500G')
// 30 more rows

[
  ['Lion', 'Linux', 'Win8'],
  ['2G', '4G', '6G', '8G'],
  ['250G', '350G', '500G']
].combinations().each{
  os, mem, disk ->
    test(os, mem, disk)
}
